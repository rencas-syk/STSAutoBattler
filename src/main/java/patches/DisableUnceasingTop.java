package patches;

import AutoBattle.AutoBattle;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.relics.UnceasingTop;

@SpirePatch2(
        clz = UnceasingTop.class,
        method = "atTurnStart"
)
public class DisableUnceasingTop {
    @SpirePostfixPatch
    public static void Postfix(UnceasingTop __instance){
        if (AutoBattle.modEnabled && !AutoBattle.keepHand) {
            __instance.disableUntilTurnEnds();
        }

    }
}
