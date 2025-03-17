package patches;


import AutoBattle.AutoBattle;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;


@SpirePatch2(clz = AbstractCard.class,method = "hasEnoughEnergy")
public class noPlay {
    @SpirePrefixPatch
    public static SpireReturn<Boolean> playDisabled(AbstractCard __instance) {
        if (AutoBattle.modEnabled) {
            return SpireReturn.Return(__instance.isInAutoplay);
        } else {
            return SpireReturn.Continue();
        }
    }
}
