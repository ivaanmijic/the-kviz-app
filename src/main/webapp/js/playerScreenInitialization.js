import { ScreenManager } from './playerScreenManager.js'
import { PlayerQuizSocket} from './playerWebSocket.js'
import { InputCodeScreen } from './playerScreens/inputCodeScreen.js'
import { InputNameScreen } from './playerScreens/inputNameScreen.js'
import { WaitingScreen } from './playerScreens/WaitingScreen.js'
import { QuestionClassicViewScreen } from './playerScreens/QuestionClassicViewScreen.js'
import { QuestionMultiViewScreen } from './playerScreens/QuestionMultiViewScreen.js'
import {FinalLeaderboardScreen} from "./playerScreens/finalLeaderboardScreen.js";

const manager = new ScreenManager("body")

const ws = new PlayerQuizSocket(manager);

document.getElementById("leaveQuiz").addEventListener('click', () => {
    ws.close();
})
document.getElementById("nextQuestion").style.display = 'none';

manager.registerScreen("inputCode", new InputCodeScreen(manager, ws));
manager.registerScreen("inputName", new InputNameScreen(manager, ws));
manager.registerScreen("waiting", new WaitingScreen(manager, ws));
manager.registerScreen("questionClassic", new QuestionClassicViewScreen(manager, ws));
manager.registerScreen("questionMultiple", new QuestionMultiViewScreen(manager, ws));
manager.registerScreen("finalLeaderboard", new FinalLeaderboardScreen(manager, ws));

manager.loadScreen("inputCode", "/inputCode")