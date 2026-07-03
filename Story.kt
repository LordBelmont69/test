package com.example.storygame

/**
 * A single choice a player can make. [requiresFlag] restricts a choice to
 * only appear if that flag was set earlier. [setsFlag] records that the
 * player made this choice, so later nodes can react to it.
 */
data class Choice(
    val text: String,
    val nextId: String,
    val requiresFlag: String? = null,
    val setsFlag: String? = null
)

/**
 * A single "page" of the story. If [choices] is empty, this node is an
 * ending.
 */
data class StoryNode(
    val id: String,
    val text: String,
    val choices: List<Choice> = emptyList(),
    val isEnding: Boolean = false
)

/**
 * The full story graph. Add new nodes here to expand the game -
 * the engine and UI don't need to change.
 */
object Story {
    const val START_ID = "start"

    val nodes: Map<String, StoryNode> = listOf(
        StoryNode(
            id = "start",
            text = "The kingdom of Veldreth has fallen silent. Its crown, " +
                "said to bind the land's ancient magic, was stolen three " +
                "nights ago. You stand at a crossroads outside the capital, " +
                "a battered map in one hand and a decision to make.",
            choices = listOf(
                Choice("Head into the Whispering Forest, where thieves are said to hide", "forest_entry"),
                Choice("Travel to the port town of Calmuth to ask around the docks", "port_entry"),
                Choice("Visit the old hermit on the hill, who knows old magic", "hermit_entry")
            )
        ),
        StoryNode(
            id = "forest_entry",
            text = "The forest is dense and unnaturally quiet. Halfway in, you " +
                "find a rusted dagger stuck in a tree stump, a warning carved " +
                "beneath it: TURN BACK.",
            choices = listOf(
                Choice("Take the dagger and press on", "forest_deeper", setsFlag = "has_dagger"),
                Choice("Leave it and press on anyway", "forest_deeper"),
                Choice("Heed the warning and turn back to the crossroads", "start")
            )
        ),
        StoryNode(
            id = "forest_deeper",
            text = "Deeper in, you stumble on a hidden camp. A hooded figure " +
                "spots you first and reaches for a blade.",
            choices = listOf(
                Choice("Fight", "forest_fight", requiresFlag = "has_dagger"),
                Choice("Try to talk your way out", "forest_talk"),
                Choice("Run", "forest_run")
            )
        ),
        StoryNode(
            id = "forest_fight",
            text = "You're faster with the dagger than you expected. The " +
                "figure yields, clutching a wound. 'The crown isn't here,' " +
                "she gasps. 'My brother took it to Calmuth to sell it to a " +
                "collector. I only wanted a cut.'",
            choices = listOf(
                Choice("Let her go and head to Calmuth", "port_entry_informed", setsFlag = "knows_buyer"),
                Choice("Press her for more before deciding what to do", "forest_interrogate")
            )
        ),
        StoryNode(
            id = "forest_talk",
            text = "You lower your hands and explain you're only looking for " +
                "the crown, not trouble. The figure relaxes slightly. 'Smart " +
                "of you not to swing first. My brother's the thief you want, " +
                "and he's fool enough to try selling it in Calmuth.'",
            choices = listOf(
                Choice("Thank her and head to Calmuth", "port_entry_informed", setsFlag = "knows_buyer"),
                Choice("Ask if she'll help you get it back", "forest_ally")
            )
        ),
        StoryNode(
            id = "forest_run",
            text = "You bolt back through the trees, lungs burning, and don't " +
                "stop until the crossroads swallows the forest behind you. " +
                "Whatever secret that camp held, it's still hidden.",
            choices = listOf(
                Choice("Try the forest again, more carefully this time", "forest_entry"),
                Choice("Give up on the forest and try somewhere else", "start")
            )
        ),
        StoryNode(
            id = "forest_interrogate",
            text = "You press the blade closer. She talks fast: her brother " +
                "is paranoid, armed, and has already found a buyer. Fear has " +
                "made her useless as an ally, but honest.",
            choices = listOf(
                Choice("Head to Calmuth with what you've learned", "port_entry_informed", setsFlag = "knows_buyer")
            )
        ),
        StoryNode(
            id = "forest_ally",
            text = "She hesitates, then nods. 'He's my blood, but he's " +
                "reckless. Fine. I'll come.' Having her at your side may " +
                "prove useful in Calmuth.",
            choices = listOf(
                Choice("Set off for Calmuth together", "port_entry_informed", setsFlag = "has_ally")
            )
        ),
        StoryNode(
            id = "port_entry",
            text = "Calmuth smells of salt and tar. Dockhands eye you " +
                "warily. Someone in this town knows where the crown went, " +
                "but no one talks to strangers for free.",
            choices = listOf(
                Choice("Bribe a dockhand for information", "port_bribe"),
                Choice("Search the taverns for rumors instead", "port_tavern")
            )
        ),
        StoryNode(
            id = "port_entry_informed",
            text = "You arrive in Calmuth already knowing what to look for: " +
                "a thief looking to sell a stolen crown to a private " +
                "collector. That narrows things considerably.",
            choices = listOf(
                Choice("Head straight for the collector's known haunts", "port_collector"),
                Choice("Ask around the docks to confirm the rumor first", "port_tavern")
            )
        ),
        StoryNode(
            id = "port_bribe",
            text = "A few coins loosen a dockhand's tongue. 'Crown, you say? " +
                "There's a collector, name of Ashworth, buys strange relics " +
                "no questions asked. Big house on the east cliff.'",
            choices = listOf(
                Choice("Head to Ashworth's house", "port_collector")
            )
        ),
        StoryNode(
            id = "port_tavern",
            text = "In a smoky tavern, a drunk sailor mutters about a nervous " +
                "man who tried to sell 'royal junk' to a collector named " +
                "Ashworth just last night.",
            choices = listOf(
                Choice("Head to Ashworth's house on the east cliff", "port_collector")
            )
        ),
        StoryNode(
            id = "port_collector",
            text = "Ashworth's house sits above the cliffs, lit by lantern " +
                "light. Through a window you see the crown itself, gleaming " +
                "on a velvet stand, guarded by two hired blades.",
            choices = listOf(
                Choice("Sneak in through the servants' entrance", "port_sneak"),
                Choice("Walk in the front door and bluff your way through", "port_bluff"),
                Choice("If you have an ally, ask them to create a distraction", "port_distraction", requiresFlag = "has_ally")
            )
        ),
        StoryNode(
            id = "port_sneak",
            text = "You slip through the shadows and past a dozing guard. " +
                "The crown is within reach when a floorboard creaks under " +
                "your foot.",
            choices = listOf(
                Choice("Grab the crown and sprint for it", "ending_daring"),
                Choice("Freeze and hope they didn't hear", "ending_caught")
            )
        ),
        StoryNode(
            id = "port_bluff",
            text = "You stride in like you own the place, claiming to be a " +
                "royal appraiser sent to verify the crown's authenticity. " +
                "Ashworth is suspicious but intrigued.",
            choices = listOf(
                Choice("Keep bluffing and ask to examine it alone", "ending_clever"),
                Choice("Lose your nerve and bolt for the door", "ending_caught")
            )
        ),
        StoryNode(
            id = "port_distraction",
            text = "Your ally slips away and returns minutes later shouting " +
                "that there's a fire in the stables. In the chaos, the " +
                "guards abandon their posts.",
            choices = listOf(
                Choice("Take the crown while everyone's distracted", "ending_ally")
            )
        ),
        StoryNode(
            id = "hermit_entry",
            text = "The hermit's hut smells of dried herbs and old parchment. " +
                "She listens to your story, then closes her eyes. 'The " +
                "crown calls to its bloodline,' she says. 'I can show you " +
                "where it rests, but the sight has a price.'",
            choices = listOf(
                Choice("Accept the price and see the vision", "hermit_vision", setsFlag = "has_vision"),
                Choice("Refuse and ask for a normal clue instead", "hermit_clue")
            )
        ),
        StoryNode(
            id = "hermit_vision",
            text = "Pain lances behind your eyes, and then you see it: the " +
                "crown, gleaming atop a cliffside house in a port town, " +
                "guarded and for sale to the highest bidder.",
            choices = listOf(
                Choice("Head to the port town, now certain of the crown's location", "port_collector")
            )
        ),
        StoryNode(
            id = "hermit_clue",
            text = "The hermit sighs. 'Stubborn as your father was. Fine — " +
                "I've heard a merchant of curiosities operates out of " +
                "Calmuth's docks. Start there.'",
            choices = listOf(
                Choice("Travel to Calmuth", "port_entry")
            )
        ),
        StoryNode(
            id = "ending_daring",
            text = "Your fingers close around the crown as shouts erupt " +
                "behind you. You crash through a window and don't stop " +
                "running until Calmuth's rooftops are far behind. Bruised " +
                "and breathless, you've done it — the crown of Veldreth is " +
                "yours to return.\n\n★ ENDING: The Daring Escape ★",
            isEnding = true
        ),
        StoryNode(
            id = "ending_caught",
            text = "Hands seize your shoulders before you can move. Ashworth " +
                "steps into the light, unimpressed. 'A thief robbing a " +
                "thief,' he says. 'How poetic.' The crown, and your " +
                "freedom, slip out of reach.\n\n★ ENDING: Caught ★",
            isEnding = true
        ),
        StoryNode(
            id = "ending_clever",
            text = "Left alone with the crown, you swap it for a convincing " +
                "fake from your pack — a trick you picked up long ago — and " +
                "walk out the front door with the real one hidden beneath " +
                "your cloak. Ashworth won't notice until it's far too late." +
                "\n\n★ ENDING: The Clever Trick ★",
            isEnding = true
        ),
        StoryNode(
            id = "ending_ally",
            text = "With the guards gone, you and your ally take the crown " +
                "together. She looks at it a long moment before handing it " +
                "to you. 'My brother's mess. Glad someone cleaned it up.' " +
                "You return to Veldreth with an unlikely friend at your " +
                "side.\n\n★ ENDING: An Unlikely Ally ★",
            isEnding = true
        )
    ).associateBy { it.id }
}
