/**
 * Created by 小虎Oni on 2016/5/19.
 */
'use strict';

app.controller('ChatDetailCtrl', ['$scope', '$stateParams', 'Chats',function($scope, $stateParams, Chats) {
  $scope.chat = Chats.get($stateParams.chatId);
}]);
