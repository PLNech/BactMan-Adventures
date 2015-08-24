package com.ionis.igem.app.game.modifiers;

import org.andengine.entity.IEntity;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.BaseTripleValueSpanModifier;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * Created by PLNech on 24/08/2015.
 */
public class ColorModifier extends BaseTripleValueSpanModifier<IEntity> {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public ColorModifier(final float pDuration, final Color pFromColor, final Color pToColor) {
        this(pDuration, pFromColor.getRed(), pToColor.getRed(), pFromColor.getGreen(), pToColor.getGreen(), pFromColor.getBlue(), pToColor.getBlue(), null, EaseLinear.getInstance());
    }

    public ColorModifier(final float pDuration, final float pFromRed, final float pToRed, final float pFromGreen, final float pToGreen, final float pFromBlue, final float pToBlue) {
        this(pDuration, pFromRed, pToRed, pFromGreen, pToGreen, pFromBlue, pToBlue, null, EaseLinear.getInstance());
    }

    public ColorModifier(final float pDuration, final Color pFromColor, final Color pToColor, final IEaseFunction pEaseFunction) {
        this(pDuration, pFromColor.getRed(), pToColor.getRed(), pFromColor.getGreen(), pToColor.getGreen(), pFromColor.getBlue(), pToColor.getBlue(), null, pEaseFunction);
    }

    public ColorModifier(final float pDuration, final float pFromRed, final float pToRed, final float pFromGreen, final float pToGreen, final float pFromBlue, final float pToBlue, final IEaseFunction pEaseFunction) {
        this(pDuration, pFromRed, pToRed, pFromGreen, pToGreen, pFromBlue, pToBlue, null, pEaseFunction);
    }

    public ColorModifier(final float pDuration, final Color pFromColor, final Color pToColor, final IModifierListener<IEntity> pEntityModifierListener) {
        super(pDuration, pFromColor.getRed(), pToColor.getRed(), pFromColor.getGreen(), pToColor.getGreen(), pFromColor.getBlue(), pToColor.getBlue(), pEntityModifierListener, EaseLinear.getInstance());
    }

    public ColorModifier(final float pDuration, final float pFromRed, final float pToRed, final float pFromGreen, final float pToGreen, final float pFromBlue, final float pToBlue, final IModifierListener<IEntity> pEntityModifierListener) {
        super(pDuration, pFromRed, pToRed, pFromGreen, pToGreen, pFromBlue, pToBlue, pEntityModifierListener, EaseLinear.getInstance());
    }

    public ColorModifier(final float pDuration, final Color pFromColor, final Color pToColor, final IModifierListener<IEntity> pEntityModifierListener, final IEaseFunction pEaseFunction) {
        super(pDuration, pFromColor.getRed(), pToColor.getRed(), pFromColor.getGreen(), pToColor.getGreen(), pFromColor.getBlue(), pToColor.getBlue(), pEntityModifierListener, pEaseFunction);
    }

    public ColorModifier(final float pDuration, final float pFromRed, final float pToRed, final float pFromGreen, final float pToGreen, final float pFromBlue, final float pToBlue, final IModifierListener<IEntity> pEntityModifierListener, final IEaseFunction pEaseFunction) {
        super(pDuration, pFromRed, pToRed, pFromGreen, pToGreen, pFromBlue, pToBlue, pEntityModifierListener, pEaseFunction);
    }

    protected ColorModifier(final ColorModifier pColorModifier) {
        super(pColorModifier);
    }

    @Override
    protected void onSetInitialValues(IEntity pItem, float pValueA, float pValueB, float pValueC) {
        pItem.setColor(pValueA, pValueB, pValueC);
    }

    @Override
    protected void onSetValues(IEntity pItem, float pPercentageDone, float pValueA, float pValueB, float pValueC) {
        pItem.setColor(pValueA, pValueB, pValueC);
    }

    @Override
    public ColorModifier deepCopy() {
        return new ColorModifier(this);
    }

}
