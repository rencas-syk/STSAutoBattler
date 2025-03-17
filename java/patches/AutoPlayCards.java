package patches;


import AutoBattle.AutoBattle;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;

@SpirePatches2({@SpirePatch2(
        clz = ShowCardAndAddToHandEffect.class,
        paramtypez = {AbstractCard.class, float.class, float.class},
        method = SpirePatch.CONSTRUCTOR
), @SpirePatch2(
        clz = ShowCardAndAddToHandEffect.class,
        paramtypez = {AbstractCard.class},
        method = SpirePatch.CONSTRUCTOR
)})
public class AutoPlayCards {

    @SpireInsertPatch(
            locator = Locator.class
    )

    public static SpireReturn<Void> autoplay(ShowCardAndAddToHandEffect __instance,AbstractCard ___card) {
        if (AutoBattle.modEnabled &&  (!(___card.type == AbstractCard.CardType.STATUS || ___card.type == AbstractCard.CardType.CURSE) || AutoBattle.playCurses)) {
            __instance.isDone = true;
            AbstractDungeon.player.hand.group.remove(___card);
            AbstractDungeon.getCurrRoom().souls.remove(___card);
            AbstractDungeon.player.limbo.group.add(___card);
            ___card.applyPowers();
            AbstractMonster target = AbstractDungeon.getCurrRoom().monsters.getRandomMonster((AbstractMonster) null, true, AbstractDungeon.cardRandomRng);
            if(___card.type == AbstractCard.CardType.STATUS){
                ___card.exhaust = true;
            }
            AbstractDungeon.actionManager.addToBottom((new NewQueueCardAction(___card, (AbstractCreature) target, false, true)));
            AbstractDungeon.actionManager.addToBottom((new UnlimboAction(___card)));
            AbstractDungeon.actionManager.addToBottom((new WaitAction(Settings.FAST_MODE ? Settings.ACTION_DUR_FASTER : Settings.ACTION_DUR_MED)));
            return SpireReturn.Return();
        } else {
            return SpireReturn.Continue();
        }
    }

    private AutoPlayCards() {
    }

    private static final class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctBehavior) throws PatchingException, CannotCompileException {
            Matcher.FieldAccessMatcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "hand");
            return LineFinder.findInOrder(ctBehavior, (Matcher)finalMatcher);
        }
        public Locator() {
        }
    }

}
