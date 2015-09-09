package com.ionis.igem.app.game;

import android.support.annotation.NonNull;
import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import com.ionis.igem.app.game.managers.ResMan;
import com.ionis.igem.app.game.model.BaseGame;
import com.ionis.igem.app.game.model.HUDElement;
import com.ionis.igem.app.game.model.res.FontAsset;
import com.ionis.igem.app.game.model.res.GFXAsset;
import com.ionis.igem.app.game.picto.Card;
import com.ionis.igem.app.utils.CalcUtils;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.*;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by PLNech on 28/08/2015.
 */
public class PictoGame extends BaseGame {
    private static final String TAG = "PictoGame";
    public static final float FAIL_DURATION = 1.0f;
    public static final float WIN_DURATION = 1.0f;
    public static final int GAME_DURATION = 60;

    private int gameScore = 0;
    private double gameScorePercent = 0;
    private int gameTime = 100;

    private boolean isDisplayingCards = false;

    private ArrayList<Card> cards = new ArrayList<>();

    private Card currentCard;
    private HUDElement HUDScore;

    private HUDElement HUDTime;
    private double cardCount;

    public PictoGame(AbstractGameActivity pActivity) {
        super(pActivity);
    }

    @Override
    public List<GFXAsset> getGraphicalAssets() {
        if (graphicalAssets.isEmpty()) {
            /* Cards */
            graphicalAssets.add(new GFXAsset(ResMan.CARD_BACK, 512, 785, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.CARD_BACK, 512, 785, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.CARD_BIOHAZARD, 512, 785, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.CARD_CMR, 512, 785, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.CARD_ENVIRONMENT, 512, 785, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.CARD_FACE, 512, 785, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.CARD_FLAMMABLE, 512, 785, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.CARD_GLOVES, 512, 785, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.CARD_MASK, 512, 785, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.CARD_OXIDISING, 512, 785, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.CARD_RADIOACTIVE, 512, 785, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.CARD_TOXIC, 512, 785, 0, 0));

            /* HUD */
            graphicalAssets.add(new GFXAsset(ResMan.HUD_TIME, 1885, 1024, 0, 0));
            graphicalAssets.add(new GFXAsset(ResMan.HUD_SCORE, 1885, 1024, 0, 0));
        }
        return graphicalAssets;
    }

    @Override
    public List<FontAsset> getFontAssets() {
        if (fontAssets.isEmpty()) {
            fontAssets.add(new FontAsset(ResMan.F_HUD_BIN, ResMan.F_HUD_BIN_SIZE, ResMan.F_HUD_BIN_COLOR, ResMan.F_HUD_BIN_ANTI));
            fontAssets.add(new FontAsset(ResMan.F_HUD_BIN, ResMan.F_HUD_BIN_SIZE, ResMan.F_HUD_PICTO_COLOR, ResMan.F_HUD_BIN_ANTI));
        }
        return fontAssets;
    }

    @Override
    public List<HUDElement> getHudElements() {
        final ITiledTextureRegion textureScore = activity.getTexture(ResMan.HUD_SCORE);
        final ITiledTextureRegion textureTime = activity.getTexture(ResMan.HUD_TIME);

        final float scale = 0.120f;

        Vector2 posS = new Vector2(5, 0); //activity.spritePosition(textureScore, 0.1f, 0.05f, HUDElement.SCALE_DEFAULT);
        Vector2 posT = new Vector2(155, 0); //activity.spritePosition(textureTime, 0.6f, 0.05f, HUDElement.SCALE_DEFAULT);

        Vector2 offS = new Vector2(90, 45);
        Vector2 offT = new Vector2(185, 45);

        IFont fontRoboto = activity.getFont(FontAsset.name(ResMan.F_HUD_BIN, ResMan.F_HUD_BIN_SIZE, ResMan.F_HUD_PICTO_COLOR, ResMan.F_HUD_BIN_ANTI));

        final VertexBufferObjectManager vbom = activity.getVBOM();

        HUDScore = new HUDElement()
                .buildSprite(posS, textureScore, vbom, scale)
                .buildText("", 8, posS.add(offS), fontRoboto, vbom)
                .setUrgent(false);
        HUDTime = new HUDElement()
                .buildSprite(posT, textureTime, vbom, scale)
                .buildText("", 8, posT.add(offT), fontRoboto, vbom)
                .setUrgent(false);

        elements.add(HUDScore);
        elements.add(HUDTime);

        return elements;
    }

    @Override
    public Scene prepareScene() {
        Scene scene = activity.getScene();

        final Background backgroundColor = new Background(0.84706f, 0.64706f, 0.84314f);
        scene.setBackground(backgroundColor);

        resetGamePoints();
        createCards();

        scene.setTouchAreaBindingOnActionDownEnabled(true);

        TimerHandler myTimer = new TimerHandler(1, true, new ITimerCallback() {
            public void onTimePassed(TimerHandler pTimerHandler) {
                decrementTime();
            }
        });
        scene.registerUpdateHandler(myTimer);
        return scene;
    }

    @Override
    public void resetGame() {
        resetGamePoints();
        for (final Card card : cards) {
            deleteCard(card, false, false);
        }
        cards.clear();
        createCards();
    }

    private void incrementScore() {
        gameScorePercent = ++gameScore * 2 * 100f / cardCount;
        Log.v(TAG, "beginContact - Increasing score to " + gameScore);
        setScore(gameScorePercent);
        if (gameScorePercent == 100) {
            activity.onWin(50 + 50 * gameTime / GAME_DURATION, 0.5f, 0.2f);
        }
    }

    private void decrementTime() {
        setTime(--gameTime);
        if (gameTime == 0) {
            activity.onLose((int) (gameScorePercent * 0.5), 0.5f, 0.2f);
        }
    }

    private void deleteCard(Card card, boolean shouldRemove, boolean didUnregisteredTouchArea) {
        final Scene scene = activity.getScene();
        final Sprite back = card.getBack();

        card.setVisible(false);
        back.setVisible(false);
        if (didUnregisteredTouchArea) {
            scene.unregisterTouchArea(card);
        }
        if (shouldRemove) {
            cards.remove(card);
        }

        scene.getChildByIndex(PortraitGameActivity.LAYER_FOREGROUND).detachChild(card);
        scene.getChildByIndex(PortraitGameActivity.LAYER_FOREGROUND).detachChild(back);
    }

    private void createCards() {
        final double marginCoeff = 1.02;
        final float rows = 5;
        final float cols = 5;
        cardCount = rows * cols;
        final Stack<String> cardStack = new Stack<>();
        final boolean countIsEven = cardCount % 2 != 0;
        if (countIsEven) {
            cardCount--; //We will remove a card.
        }

        final float cardWidth = (int) Math.ceil(512f * Card.SCALE_DEFAULT * marginCoeff);
        final float cardHeight = (int) Math.ceil(785f * Card.SCALE_DEFAULT * marginCoeff);
        final float baseX = 15;
        final float baseY = 110;

        Log.d(TAG, "createCards - cardW:" + cardWidth + ", H:" + cardHeight);
        Log.d(TAG, "createCards - baseX:" + baseX + ", Y:" + baseY);
//        final float stepX = (GameActivity.CAMERA_WIDTH - baseX) / cardWidth;
//        final float stepY = (GameActivity.CAMERA_HEIGHT - baseY) / cardHeight;
//        final float rows = Math.min(stepX, stepY);
//        Log.d(TAG, "createCards - stepX:" + stepX + ", stepY:" + stepY);

        ArrayList<String> cardList = new ArrayList<>();
        for (int i = 0; i < cardCount / 2; i++) {
            cardList.add(randomCardName());
        }
        cardStack.addAll(cardList);
        Collections.shuffle(cardList);
        cardStack.addAll(cardList);

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                final boolean isMiddleCard = i == Math.floor(cols / 2) && j == Math.floor(rows / 2);
                if (countIsEven && isMiddleCard) {
                    Log.d(TAG, "createCards - Skipping card " + i + "," + j + " to preserve evenness");
                } else {
                    createCard(cardStack.pop(), baseX + cardWidth * i, baseY + cardHeight * j);
                }
            }
        }

//        Card debugCard = new Card(activity.getCamera().getWidth() / 2 - 40, baseY / 2 - 80, ResMan.CARD_BACK, activity.getTexture(ResMan.CARD_BACK), activity) {
//            @Override
//            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
//                final int action = pSceneTouchEvent.getAction();
//                if (action == TouchEvent.ACTION_DOWN) {
//                    for (Card card : cards) {
//                        card.flip();
//                    }
//                    return true;
//                }
//                return false;
//            }
//        };
//        addCard(debugCard);
    }

    @NonNull
    private String randomCardName() {
        String resName = ResMan.CARD_BACK;

        int randCardIndex = CalcUtils.randomOf(ResMan.CARD_COUNT, random);
        switch (randCardIndex) {
            case 0:
                resName = ResMan.CARD_BIOHAZARD;
                break;
            case 1:
                resName = ResMan.CARD_CMR;
                break;
            case 2:
                resName = ResMan.CARD_ENVIRONMENT;
                break;
            case 3:
                resName = ResMan.CARD_FACE;
                break;
            case 4:
                resName = ResMan.CARD_FLAMMABLE;
                break;
            case 5:
                resName = ResMan.CARD_GLOVES;
                break;
            case 6:
                resName = ResMan.CARD_MASK;
                break;
            case 7:
                resName = ResMan.CARD_OXIDISING;
                break;
            case 8:
                resName = ResMan.CARD_RADIOACTIVE;
                break;
            case 9:
                resName = ResMan.CARD_TOXIC;
                break;
        }
        return resName;
    }

    private void createCard(float pX, float pY) {
        String resName = randomCardName();
        createCard(resName, pX, pY);
    }

    private void createCard(String resCardName, float pX, float pY) {
        Card card = new Card(pX, pY, resCardName, activity.getTexture(resCardName), activity);
        addCard(card);
    }

    private void addCard(Card card) {
        cards.add(card);
        final Scene gameScene = activity.getScene();
        gameScene.getChildByIndex(PortraitGameActivity.LAYER_FOREGROUND).attachChild(card);
        gameScene.getChildByIndex(PortraitGameActivity.LAYER_FOREGROUND).attachChild(card.getBack());
        gameScene.registerTouchArea(card);
    }

    public void onTouchCard(final Card card) {
        if (currentCard == null) {
            Log.d(TAG, "onTouchCard - First card: " + card.getType());
            currentCard = card;
            card.flip();
        } else {
            final String cardTypes = currentCard.getType() + " - " + card.getType();

            Log.d(TAG, "onTouchCard - Second card: " + card.getType());
            card.flip();

            if (card.equals(currentCard)) {
                Log.d(TAG, "onTimePassed - Same card :O");
                currentCard = null;
            } else if (card.getType().equals(currentCard.getType())) {
                Log.d(TAG, "onTimePassed - Same types :)" + cardTypes);
                final Card oldCard = currentCard;
                currentCard = null;
                incrementScore();

                animateCardDeletion(card);
                animateCardDeletion(oldCard);
            } else {
                Log.d(TAG, "onTimePassed - =/= types :( " + cardTypes);
                isDisplayingCards = true;
                activity.registerUpdateHandler(FAIL_DURATION, new ITimerCallback() {
                    @Override
                    public void onTimePassed(TimerHandler pTimerHandler) {
                        card.flip();
                        currentCard.flip();
                        currentCard = null;
                        isDisplayingCards = false;
                    }
                });
            }
        }
    }

    private void resetGamePoints() {
        gameScore = 0;
        gameTime = GAME_DURATION;
        setScore(gameScore);
        setTime(gameTime);
        HUDTime.setUrgent(false);
    }

    private void setScore(double score) {
        final boolean isInteger = score == Math.floor(score);
        String padding = "";
        if (score == 0) padding += " ";
        if (score < 10) padding += " ";
        if (isInteger) padding += " ";
        final String formatStr = isInteger ? "%.0f%%" : "%.1f%%";
        final String scoreStr = padding + String.format(formatStr, score);
        Log.d(TAG, "setScore - Setting score to _" + scoreStr + "_");
        setScore(scoreStr);
    }

    private void setScore(CharSequence text) {
        HUDScore.getText().setText(text);
    }

    private void setTime(int time) {
        String padding = "";
        if (time < 10) {
            padding += " ";
            HUDTime.setUrgent(true);
        }
        if (time < 100) padding += " ";
        setTime(padding + time);
    }

    private void setTime(CharSequence text) {
        HUDTime.getText().setText(text);
    }

    public boolean isDisplayingCards() {
        return isDisplayingCards;
    }

    private void animateCardDeletion(final Card card) {
        activity.getScene().unregisterTouchArea(card);

        final IEntityModifier.IEntityModifierListener logListener = new IEntityModifier.IEntityModifierListener() {
            @Override
            public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
            }

            @Override
            public void onModifierFinished(final IModifier<IEntity> pEntityModifier, final IEntity pEntity) {
                activity.runOnUpdateThread(new Runnable() {
                    @Override
                    public void run() {
                        deleteCard(card, true, true);
                        Log.d(TAG, "run - Animation finished, deleting card " + card.getType());
                    }
                });
            }
        };
        final float fromAlpha = card.getAlpha();
        final float toAlpha = 0;

        final SequenceEntityModifier entityModifier = new SequenceEntityModifier(
                new AlphaModifier(WIN_DURATION, fromAlpha, toAlpha),
                new DelayModifier(WIN_DURATION)
        );

        card.registerEntityModifier(new LoopEntityModifier(entityModifier, 1, logListener));
    }
}
