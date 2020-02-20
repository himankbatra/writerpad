'use strict'

angular.module('writerpad.services', []).factory('WriterpadService',
		[ "$http", "CONSTANTS", function($http, CONSTANTS) {
			var service = {};
			service.getArticleById = function(slugId) {
				var url = CONSTANTS.getUserByIdUrl + slugId;
				return $http.get(url);
			}
			service.getAllArticles = function() {
				return $http.get(CONSTANTS.getAllArticles);
			}
			service.saveUser = function(userDto) {
				return $http.post(CONSTANTS.saveUser, userDto);
			}
			service.saveArticle = function(articleDto) {
            				return $http.post(CONSTANTS.saveArticle, articleDto);
            			}
			return service;
		} ]);