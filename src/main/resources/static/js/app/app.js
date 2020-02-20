'use strict'

var demoApp = angular.module('writerpad', [ 'ui.bootstrap', 'writerpad.controllers',
		'writerpad.services']);
demoApp.constant("CONSTANTS", {
	getArticleByIdUrl : "/api/articles/",
	getAllArticles : "/api/articles",
	saveUser : "/api/users",
	saveArticle: "/api/articles"
});

function getCookie(cname) {
  var name = cname + "=";
  var ca = document.cookie.split(';');
  for(var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}