/**
 * Created by 小虎Oni on 2016/5/19.
 */
'use strict';

app.constant('JS_REQUIRES', {
    //*** Scripts
    scripts: {
        //*** Controllers
        'AccountCtrl':"js/controllers/AccountCtrl.js",
        'ChatDetailCtrl':"js/controllers/ChatDetailCtrl.js",
        'ChatsCtrl':"js/controllers/ChatsCtrl.js",
        'DashCtrl':"js/controllers/DashCtrl.js",
        //*** Services
        'ChatsService':"js/services/ChatsService.js"
        //***  工具类
        //*** 主件
    },
    ViewArgs: {
      ChatsArgs: ['ChatsCtrl','ChatsService'],
      ChatDetailCtrlArgs: ['ChatDetailCtrl','ChatsService']
    }
});
