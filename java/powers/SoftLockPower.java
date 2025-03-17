package powers;

import AutoBattle.AutoBattle;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;

public class SoftLockPower extends AbstractPower {
    public static final String POWER_ID = "SoftLockPower";
    public static final String NAME;
    public static final String DESCRIPTION;

    public SoftLockPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 0;
        this.updateDescription();
        this.loadRegion("time");
        this.type = PowerType.BUFF;
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
    }

    public void updateDescription() {
        this.description = DESCRIPTION;
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if(AutoBattle.modEnabled && AbstractDungeon.player.endTurnQueued){
            this.playApplyPowerSfx();
            AbstractDungeon.actionManager.callEndTurnEarlySequence();
            CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
            AbstractDungeon.topLevelEffectsQueue.add(new TimeWarpTurnEndEffect());
        }
    }
    /*
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (AutoBattle.modEnabled && card.type == AbstractCard.CardType.STATUS) {
            card.exhaust = true;
            action.exhaustCard = true;
        }
    }
    */
    static {
        NAME = "Having a Heart Time?";
        DESCRIPTION = "Allows you to end your turn whenever you see fit.";
    }


}
