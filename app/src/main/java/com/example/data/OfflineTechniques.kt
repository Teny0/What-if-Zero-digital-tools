package com.example.data

object OfflineTechniques {
    val categories: List<TechniqueCategory> = listOf(
        TechniqueCategory(
            id = 1,
            title = "Word-of-Mouth & Referrals",
            emoji = "🗣️",
            subtitle = "Scripts, incentives & perfect timing",
            description = "Harness existing happy clients to bring friends with exact word-for-word scripts and simple paper referral cards.",
            samplePrompts = listOf(
                "Write a 2-sentence referral script I can say to customers right after they praise my service.",
                "Design a simple index-card referral incentive program where both parties get a tangible perk.",
                "What is the exact timing and wording to ask for referrals 14 days after a project wraps up?"
            ),
            quickOfflineTips = listOf(
                "Ask at the 'Peak of Delight' — immediately after the customer says 'Thank you so much!'",
                "Give them two physical referral cards: one for their wallet, one to hand to a neighbor.",
                "Reciprocity rule: Reward the referrer with a handwritten note and a small physical gift (coffee card, baked treat)."
            )
        ),
        TechniqueCategory(
            id = 2,
            title = "In-Person Networking",
            emoji = "🤝",
            subtitle = "Elevator pitches & cold intro scripts",
            description = "Make local business mixers, Chamber of Commerce meetings, and morning meetups pay off without awkwardness.",
            samplePrompts = listOf(
                "Give me a 15-second elevator pitch that focuses on client pain points instead of my job title.",
                "What's a natural icebreaker script to start a conversation with someone standing alone at a mixer?",
                "Draft a phone follow-up script to call a contact 24 hours after meeting them at a local event."
            ),
            quickOfflineTips = listOf(
                "Never pitch first; ask 'What brings you here today and who is a dream customer for you?'",
                "Write notes on the back of their business card immediately after stepping away (spouses, interests, follow-up promise).",
                "Call or handwrite a note within 48 hours referencing that specific detail."
            )
        ),
        TechniqueCategory(
            id = 3,
            title = "Print & Physical Materials",
            emoji = "📄",
            subtitle = "Flyers, door hangers & business cards",
            description = "High-converting copy and physical layout descriptions for print shops or DIY handcrafting.",
            samplePrompts = listOf(
                "Write the exact headline and bullet copy for a door-hanger targeting local homeowners.",
                "Design the front and back copy for a standard 3.5x2 inch business card that acts as a mini-sales letter.",
                "Draft a community bulletin board tear-off flyer with high-contrast text and 10 tear tabs."
            ),
            quickOfflineTips = listOf(
                "Headline must state the #1 problem solved, not your business name.",
                "Include a clear physical Call to Action with a local phone number and deadline.",
                "Print on heavy, textured 100lb cardstock or kraft paper — tactile weight signals quality."
            )
        ),
        TechniqueCategory(
            id = 4,
            title = "Direct Mail & Handwritten Notes",
            emoji = "✉️",
            subtitle = "Postcards, letters & sequence mail",
            description = "Stand out in a mailbox full of junk with warm, personalized pen-on-paper mail that gets opened 99% of the time.",
            samplePrompts = listOf(
                "Write a 3-part handwritten follow-up letter sequence for prospects who didn't buy on the first visit.",
                "Draft a warm, personal postcard copy to send to past customers who haven't returned in 90 days.",
                "Write a genuine handwritten thank-you note to send new customers the day after their purchase."
            ),
            quickOfflineTips = listOf(
                "Use real postage stamps (commemorative ones) and blue ballpoint ink so it looks personal.",
                "Never use window envelopes or pre-printed corporate labels.",
                "Keep the tone warm and conversational, like writing to a trusted acquaintance."
            )
        ),
        TechniqueCategory(
            id = 5,
            title = "Local Partnerships & Co-Marketing",
            emoji = "🏪",
            subtitle = "Cross-promotions & bundle swaps",
            description = "Team up with non-competing businesses that share your exact target audience for zero-cost client sharing.",
            samplePrompts = listOf(
                "Give me a walk-in pitch script to propose a joint promotion with a neighboring complementary shop.",
                "Draft a counter-top display card promoting a bundle deal between my business and a partner.",
                "What are 5 non-competing local businesses that share my ideal customers, and what can we swap?"
            ),
            quickOfflineTips = listOf(
                "Target businesses where their customer is one step before or after using your service.",
                "Propose a trial period of 30 days so neither business feels locked in.",
                "Always offer to display their materials or promote their offer first to trigger reciprocity."
            )
        ),
        TechniqueCategory(
            id = 6,
            title = "Community & Sponsorship",
            emoji = "🎪",
            subtitle = "Pop-ups, booths & local events",
            description = "Become a beloved local fixture by showing up at farmers markets, school events, and charity drives.",
            samplePrompts = listOf(
                "Design an interactive physical booth game/activity that collects customer phone numbers without feeling pushy.",
                "Write a pitch letter to sponsor a local Little League team or school drama club with maximum visibility.",
                "Draft a pop-up weekend table checklist and greeting script to stop foot traffic at a street market."
            ),
            quickOfflineTips = listOf(
                "Don't just stand behind a table looking at your phone; stand out front and offer a tangible sample or game.",
                "Run a physical fishbowl raffle where people drop their business cards or phone numbers for a prize.",
                "Announce the winner over a phone call within 48 hours and give every non-winner a runner-up voucher."
            )
        ),
        TechniqueCategory(
            id = 7,
            title = "Signage & Storefront",
            emoji = "🪧",
            subtitle = "Sandwich boards, window art & yard signs",
            description = "Turn physical foot and car traffic into paying customers with witty, high-visibility street signage.",
            samplePrompts = listOf(
                "Give me 5 funny, attention-grabbing chalkboard sandwich board slogans for foot traffic outside my door.",
                "Write high-impact copy for a 24x18 inch corrugated yard sign that can be read by drivers in 3 seconds.",
                "Draft bold window lettering ideas that highlight our single best-selling feature or guarantee."
            ),
            quickOfflineTips = listOf(
                "Driver rule: Signs must be readable at 35mph — maximum 6 words and 4-inch tall block letters.",
                "Chalkboard humor: Rotate quotes weekly; locals will look forward to walking past your storefront.",
                "Use high contrast: Black lettering on bright yellow or white on deep chalkboard."
            )
        ),
        TechniqueCategory(
            id = 8,
            title = "PR & Earned Media",
            emoji = "📰",
            subtitle = "Local newspaper pitches & radio spots",
            description = "Get featured in community gazettes, morning radio shows, and local news without paying an agency.",
            samplePrompts = listOf(
                "Write a 1-page press release pitch to a local newspaper editor about an unusual community milestone.",
                "Draft a 30-second telephone pitch to call a local morning radio host with a timely story hook.",
                "How do I position a small business announcement as a compelling human-interest story for the local press?"
            ),
            quickOfflineTips = listOf(
                "Reporters don't care about your new product; they care about how it affects real local residents.",
                "Pitch on Tuesday or Wednesday mornings between 9:30 AM and 10:30 AM after morning editorial meetings.",
                "Include a ready-to-quote founder soundbite so the reporter has less writing to do."
            )
        ),
        TechniqueCategory(
            id = 9,
            title = "Sales Conversations & Scripts",
            emoji = "📞",
            subtitle = "Phone calls, door-to-door & objection handling",
            description = "Master authentic, high-empathy sales conversations that close deals without high pressure.",
            samplePrompts = listOf(
                "Write a warm phone script to call past prospects who went silent, without sounding desperate.",
                "Give me 3 practical scripts to handle the objection: 'That's too expensive right now.'",
                "Draft a friendly door-to-door neighborhood introduction script for a home service provider."
            ),
            quickOfflineTips = listOf(
                "Start calls with 'Did I catch you in the middle of anything?' rather than 'How are you today?'",
                "Acknowledge objections first ('I completely understand that budget is top of mind') before explaining value.",
                "Always end with an alternative choice question: 'Would Tuesday morning or Thursday afternoon suit you better?'"
            )
        ),
        TechniqueCategory(
            id = 10,
            title = "Loyalty & Retention",
            emoji = "🎟️",
            subtitle = "Punch cards, VIP treatment & milestone notes",
            description = "Keep customers coming back for life with tactile rewards and unforgettable old-school hospitality.",
            samplePrompts = listOf(
                "Design a physical punch card system with the 'Endowed Progress Effect' (pre-stamped first 2 slots).",
                "Write a template for an annual handwritten client anniversary or birthday greeting card.",
                "Create an exclusive 'VIP Secret Menu' or perks card handed only to top 10% patrons."
            ),
            quickOfflineTips = listOf(
                "Endowed progress: Give a 10-punch card with the first 2 punches already completed — completion rates double.",
                "Surprise and delight: Give an unexpected freebie on visit 4 instead of waiting for visit 10.",
                "Handwritten cards on personal milestones are kept on desks and refrigerator doors for years."
            )
        ),
        TechniqueCategory(
            id = 11,
            title = "Public Speaking & Workshops",
            emoji = "🎤",
            subtitle = "Free talks, live demos & community classes",
            description = "Establish instant authority in your town by teaching a free 30-minute workshop at a local library or venue.",
            samplePrompts = listOf(
                "Outline a 30-minute free community workshop that naturally leads attendees to want to hire me.",
                "Draft a flyer and telephone pitch to local libraries or community centers offering a free educational seminar.",
                "Write the opening 3 minutes of a live demonstration talk that hooks audience attention immediately."
            ),
            quickOfflineTips = listOf(
                "Teach the 'What' and 'Why' generously; let your paid service handle the difficult 'How'.",
                "Hand out a 1-page printed worksheet or checklist at the start so attendees take notes with your contact info.",
                "End with an exclusive in-person consultation offer valid only for attendees who sign up that day."
            )
        ),
        TechniqueCategory(
            id = 12,
            title = "Guerrilla & Creative Tactics",
            emoji = "🎨",
            subtitle = "Sidewalk chalk, physical stunts & contests",
            description = "Memorable, pennies-on-the-dollar stunts that get the whole neighborhood talking.",
            samplePrompts = listOf(
                "Give me 3 creative sidewalk chalk messaging ideas that direct foot traffic around the corner to my shop.",
                "Design an offline scavenger hunt or physical mystery coupon drop around town.",
                "Draft a low-cost, high-fun window guessing contest (e.g., jellybean jar) that collects 100+ local contacts."
            ),
            quickOfflineTips = listOf(
                "Sidewalk chalk arrows or footprints leading to your entrance create an irresistible physical curiosity loop.",
                "Window contests: People love guessing games; require a name and phone number on an entry slip to enter.",
                "Always check local municipal rules regarding sidewalk chalk on public easements."
            )
        )
    )
}
