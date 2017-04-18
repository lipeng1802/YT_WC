/**
 * Created by 小虎Oni on 2016/5/19.
 */
'use strict';

app.controller('ChatsCtrl', ['$scope', 'Chats',function($scope, Chats) {
  $scope.chats = Chats.all();
  $scope.remove = function(chat) {
    Chats.remove(chat);
  };
}]);
