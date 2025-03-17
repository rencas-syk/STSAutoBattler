package AutoBattle;
//When all External Libraries are added from the pom.xml using maven the code can be uncommented.


import basemod.*;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import powers.SoftLockPower;


@SpireInitializer
public class ModInitializer implements PostInitializeSubscriber, OnPlayerTurnStartPostDrawSubscriber, OnStartBattleSubscriber {


    public ModInitializer(){
        //Use this for when you subscribe to any hooks offered by BaseMod.
        BaseMod.subscribe(this);
    }

    //Used by @SpireInitializer
    public static void initialize(){
        //This creates an instance of our classes and gets our code going after BaseMod and ModTheSpire initialize.
        ModInitializer modInitializer = new ModInitializer();
        AutoBattle.initialize();
    }

    @Override
    public void receivePostInitialize() {
        ModPanel settingsPanel = new ModPanel();
        ModLabeledToggleButton enableMod = new ModLabeledToggleButton("Enable this Mod", 350.0F, 600.0F, Settings.CREAM_COLOR, FontHelper.charDescFont, AutoBattle.modEnabled, settingsPanel, (label) -> {
        }, (button) -> {
            AutoBattle.modEnabled = button.enabled;
            AutoBattle.setBoolean("modEnabled", button.enabled);
        });
        settingsPanel.addUIElement(enableMod);

        ModLabeledToggleButton playCurses = new ModLabeledToggleButton("Autoplay curses and status cards (unless drawn in Mimic Mayhem mode) (i.e. they have no effect)", 350.0F, 500.0F, Settings.CREAM_COLOR, FontHelper.charDescFont, AutoBattle.playCurses, settingsPanel, (label) -> {
        }, (button) -> {
            AutoBattle.playCurses = button.enabled;
            AutoBattle.setBoolean("playCurses", button.enabled);
        });
        settingsPanel.addUIElement(playCurses);

        ModLabeledToggleButton keepHand = new ModLabeledToggleButton("Mimic Mayhem: Draw cards, then play as many from your draw pile (turn start only)", 350.0F, 550.0F, Settings.CREAM_COLOR, FontHelper.charDescFont, AutoBattle.keepHand, settingsPanel, (label) -> {
        }, (button) -> {
            AutoBattle.keepHand = button.enabled;
            AutoBattle.setBoolean("keepHand", button.enabled);
        });
        settingsPanel.addUIElement(keepHand);

        Texture badge = ImageMaster.loadImage("img/AutoBattleModBadge.png");
        BaseMod.registerModBadge(badge, "AutoBattle", "HighQualityToast", "shoddy AutoBattler", settingsPanel);

    }

    @Override
    public void receiveOnPlayerTurnStartPostDraw() {
        if (AutoBattle.modEnabled && AutoBattle.keepHand){
            AbstractDungeon.actionManager.addToTop(new WaitAction(.1F));
            for(int i = 0; i < AbstractDungeon.player.masterHandSize; ++i) {
                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                    public void update() {
                        this.addToBot(new PlayTopCardAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng), false));
                        this.isDone = true;
                    }
                });
            }
        }
    }


    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SoftLockPower(AbstractDungeon.player)));
    }


}
