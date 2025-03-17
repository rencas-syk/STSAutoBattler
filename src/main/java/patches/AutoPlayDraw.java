package patches;


import AutoBattle.AutoBattle;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.Iterator;

@SpirePatch2(
        clz = AbstractPlayer.class,
        paramtypez = {int.class},
        method = "draw"
)
public class AutoPlayDraw {
    @SpireInsertPatch(
            locator = Locator.class
    )
    private static SpireReturn<Void> Insert(AbstractPlayer __instance){
        if (AutoBattle.modEnabled && !AutoBattle.keepHand) {
            AbstractCard card = __instance.drawPile.getTopCard();

            if(!AutoBattle.playCurses && (card.type == AbstractCard.CardType.STATUS || card.type == AbstractCard.CardType.CURSE)){
                card.current_x = CardGroup.DRAW_PILE_X;
                card.current_y = CardGroup.DRAW_PILE_Y;
                card.setAngle(0.0F, true);
                card.lighten(false);
                card.drawScale = 0.12F;
                card.targetDrawScale = 0.75F;
                card.triggerWhenDrawn();
                __instance.hand.addToHand(card);
                __instance.drawPile.removeTopCard();
                Iterator var4 = __instance.powers.iterator();

                while(var4.hasNext()) {
                    AbstractPower p = (AbstractPower)var4.next();
                    p.onCardDraw(card);
                }

                var4 = __instance.relics.iterator();

                while(var4.hasNext()) {
                    AbstractRelic r = (AbstractRelic) var4.next();
                    r.onCardDraw(card);
                }
                return SpireReturn.Return();
            }

            card.current_x = CardGroup.DRAW_PILE_X;
            card.current_y = CardGroup.DRAW_PILE_Y;
            card.setAngle(0.0F, true);
            card.lighten(false);
            card.drawScale = 0.12F;
            card.targetDrawScale = 0.75F;
            card.triggerWhenDrawn();
            __instance.hand.addToHand(card);
            __instance.drawPile.removeTopCard();

            AbstractDungeon.player.hand.group.remove(card);
            AbstractDungeon.getCurrRoom().souls.remove(card);
            AbstractDungeon.player.limbo.group.add(card);
            card.applyPowers();
            AbstractMonster target = AbstractDungeon.getCurrRoom().monsters.getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
            if(card.type == AbstractCard.CardType.STATUS) {
                card.exhaust = true;
            }
            AbstractDungeon.actionManager.addToBottom((new NewQueueCardAction(card, (AbstractCreature)target, false, true)));
            AbstractDungeon.actionManager.addToBottom((new UnlimboAction(card)));
            AbstractDungeon.actionManager.addToBottom((new WaitAction(Settings.FAST_MODE ? Settings.ACTION_DUR_FASTER : Settings.ACTION_DUR_MED)));



            Iterator var4 = __instance.powers.iterator();

            while(var4.hasNext()) {
                AbstractPower p = (AbstractPower)var4.next();
                p.onCardDraw(card);
            }

            var4 = __instance.relics.iterator();

            while(var4.hasNext()) {
                AbstractRelic r = (AbstractRelic)var4.next();
                r.onCardDraw(card);
            }
            return SpireReturn.Return();
        } else {
            return SpireReturn.Continue();
        }

    }

    private static final class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctBehavior) throws PatchingException, CannotCompileException {
            Matcher.MethodCallMatcher finalMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "getTopCard");
            return LineFinder.findInOrder(ctBehavior, (Matcher)finalMatcher);
        }
        public Locator() {
        }
    }


}
