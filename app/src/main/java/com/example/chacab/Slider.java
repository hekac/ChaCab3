package com.example.chacab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class Slider extends View {

    //Valeur par défaut
    final static float DEF_CERCLE1_D = 120;
    final static float DEF_CERCLE2_D = 100;
    final static float DEF_CERCLE3_D = 40;

    //Dimension min
    final static float MIN_CERCLE1_D = 120;
    final static float MIN_CERCLE2_D = 100;
    final static float MIN_CERCLE3_D = 40;

    //Float Diametre
    private float mCursor1Diameter;
    private float mCursor2Diameter;
    private float mCursor3Diameter;

    // Couleurs
    private int mCERCLE1;
    private int mCERCLE2;
    private int mCERCLE3;
    private int mCERCLE4;

    // Pinceaux
    private Paint mC1Color = null;
    private Paint mC2Color = null;
    private Paint mC3Color = null;
    private Paint mC4Color = null;

    // Valeur du Slider
    private float mValue = 50;

    // Valeur minimale du Slider
    private float mMin = 0;

    // Etat du Slider
    private boolean mEnabled = true;

    // Valeur maximale du Slider
    private float mMax = 100;

    // Etats spécifiques aux actions
    private boolean isDoubleClick = false;
    private boolean moveActionDisabled = false;
    /**
     * Constructeur statique (via XML)
     * @param context : contexte du slider
     */
    public Slider(Context context) {
        super(context);
        init(context,null);
    }

    /**
     * Constructeur statique (via XML)
     * @param context : contexte du slider
     * @param attrs : paramètres de personnalisation issus du XML
     */
    public Slider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    /**
     * Initialisation du Slider : code mutualisé entre constructeurs
     * @param context
     * @param attributeSet
     */
    private void init(Context context, AttributeSet attributeSet){

        // Initialisation des dimensions par défaut en pixel
        mCursor1Diameter = dpToPixel(DEF_CERCLE1_D);
        mCursor2Diameter = dpToPixel(DEF_CERCLE2_D);
        mCursor3Diameter = dpToPixel(DEF_CERCLE3_D);

// pinceaux
        mC1Color = new Paint();
        mC2Color = new Paint();
        mC3Color = new Paint();
        mC4Color = new Paint();

        // Suppression du repliement
        mC1Color.setAntiAlias(true);
        mC2Color.setAntiAlias(true);
        mC3Color.setAntiAlias(true);

        // Application du style (plein)
        mC1Color.setStyle(Paint.Style.FILL_AND_STROKE);
        mC2Color.setStyle(Paint.Style.FILL_AND_STROKE);
        mC3Color.setStyle(Paint.Style.FILL_AND_STROKE);


        // Couleurs par défaut
        mCERCLE1 = ContextCompat.getColor(context, R.color.colorAccent);
        mCERCLE2 = ContextCompat.getColor(context, R.color.colorPrimary);
        mCERCLE3 = ContextCompat.getColor(context, R.color.colorSecondary);
        mCERCLE4 = ContextCompat.getColor(context, R.color.colorPrimaryDark);

        mC1Color.setColor(mCERCLE1);
        mC2Color.setColor(mCERCLE2);
        mC3Color.setColor(mCERCLE3);
        mC4Color.setColor(mCERCLE4);

        // Spécification des terminaisons
      //  mBarPaint.setStrokeCap(Paint.Cap.ROUND);
      //  mValueBarPaint.setStrokeCap(Paint.Cap.ROUND);



        // Fixe les largeurs
        //mBarPaint.setStrokeWidth(mBarWidth);
       // mValueBarPaint.setStrokeWidth(mBarWidth);

    }

    /**
     * Wrapper passage DIP en pixels
     * @param valueInDp dimension en DIP
     * @return dimension en pixels
     */
    private float dpToPixel(float valueInDp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, getResources().getDisplayMetrics());
    }


    /**
     * Normalisation de la valeur entre 0 et 1.
     * @param value
     * @return
     */
    private float valueToRatio(float value){

        return (value - mMin) / (mMax - mMin);
    }

    /**
     * Dénormalisation vers la valeur du curseur
     * @param ratio : valeur entre 0 (min) et 1 (max)
     * @return valeur associée au Slider
     */
    private float ratioToValue(float ratio) {

        return ratio * (mMax - mMin) + mMin;
    }



    /**
     * Convertions d'une position écran en une valeur du Slider
     * @param position : position écran
     * @return valeur slider
     */
    private float toValue(Point position){
        float ratio = 1 - (position.y -getPaddingTop()-mCursor2Diameter/2);
        if(ratio < 0) ratio = 0;
        if(ratio > 1) ratio = 1;
        return ratioToValue(ratio);

    }

    /**
     * Redéfinition de la méthode de tracé du Slider
     * @param canvas : zone de tracé
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Point centre = new Point();
        centre.x= getWidth()/2;
        centre.y= getHeight()/2;


        canvas.drawCircle(centre.x, centre.y, mCursor1Diameter , mC1Color);
        canvas.drawCircle(centre.x, centre.y, mCursor2Diameter , mC2Color);
        canvas.drawCircle(centre.x, centre.y, mCursor3Diameter , mC3Color);


        mC4Color.setStrokeWidth (mCursor2Diameter-mCursor3Diameter);
        mC4Color.setStyle (Paint.Style.STROKE);

        float radius = mCursor1Diameter-mCursor2Diameter/2;
        canvas.drawArc(centre.x-radius, centre.y-radius, centre.x+radius ,centre.y+radius,270,i,true,mC4Color);


    }
    protected double Position(float X, float Y) {
        double i;
        i=Math.atan2(X,Y);
        return (i);
    }



    /*************************************************
     * Interface
     ************************************************/



    private SliderChangeListener mSliderChangeListener; /**< Référence vers le listener*/

    /**
     * Setter de listener
     * @param sliderChangeListener
     */

    public void setSliderChangeListener(SliderChangeListener sliderChangeListener){
        mSliderChangeListener = sliderChangeListener;
    }

    /**
     * Interface Listener
     */
    public interface SliderChangeListener{
        void onChange(float value);
        void onDoubleClick(float value);
    }


    /*************************************************
     * ACTIONS
     ************************************************/

    /**
     * Redéfinition de la gestion des actions
     * @param event : type d'action et paramètres associés
     * @return : indique que le gestionnaire a consommé l'évènement
     * sinon transfert de l'évènement aux éventuels Views en overlap
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()) {
            case MotionEvent.ACTION_MOVE :
                if(! moveActionDisabled) {
                    mSliderChangeListener.onChange(mValue);
                    mValue = toValue(new Point((int) event.getX(), (int) event.getY()));
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_DOWN :
                moveActionDisabled = true;
                if(isDoubleClick){
                    mValue = mMin;
                    mSliderChangeListener.onDoubleClick(mValue);
                    invalidate();
                } else
                    isDoubleClick = true;
                break;
        }
        return true;
    }
}
