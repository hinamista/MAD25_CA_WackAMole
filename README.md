# MAD25_CA_WackAMole
This is a development done to satisfy the 
assignment portion for module “Mobile App development” under 
Ngee Ann Polytechnic for AY24/25

# LLM Usage Declaration and Reflection

This document declares and explains the use of Large Language Models (LLMs) during the development of the **Whack-A-Mole (Advanced)** Android application, in accordance with **Section 5: LLM Usage** of the assessment guidelines.

---

## a. Use of Large Language Models (LLMs)

### Tools Used
- **ChatGPT (OpenAI)**

### Type of LLM Use
The LLM was used strictly as a **development support tool**, not as a substitute for completing the assignment.
All final implementation decisions, code structure, logic, and testing were carried out manually by the developer.

### How LLMs Were Used
The LLM was used in the following ways:
- Explaining compiler errors, Gradle dependency issues (KAPT vs KSP), and runtime crashes.
- Brainstorming UI layout ideas for Login, Game, Settings, and Leaderboard screens.
- Suggesting alternative implementations for **small code fragments**, which were then adapted and integrated manually.
- (FYI the developer also tried to make a cute mole animation with a 2D pixel art spritesheet that she created herself but she was unable to implement it even with the help of ChatGPT)

---

### Example Prompts Used
1. *“Why is Android Studio complaining about the KSP plugin and how can I fix it?”*  
2. *“How can I prevent double scoring when a mole appears multiple times?”*  

These prompts were focused on **understanding concepts** and **resolving specific development issues**, rather than requesting complete solutions.

---

## b. Sample Code Generated / Refactored and Explanation

### Example 1: Preventing Double Scoring (Game Logic)

**Initial (Problematic) Code**
if (index == moleIndex) {
    score += 1
}

**Refactored Code**
if (index == moleIndex && lastScoredToken != moleToken) {
    score += 1
    lastScoredToken = moleToken
}

### Example 2: Mole Timing and Movement Logic

**Initial Implementation**
delay(1000L)
moleIndex = (0..8).random()

**Refactored Code**
val nextDelay = (700..1000).random()
delay(nextDelay.toLong())
moleIndex = (0..8).random()

---

## Key Takeaways / Lessons Learned
LLMs are most effective as learning, debugging, and reasoning tools
Blindly copying AI-generated code leads to bugs and poor structure
