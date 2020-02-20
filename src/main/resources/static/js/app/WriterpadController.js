'use strict'

var module = angular.module('writerpad.controllers', ['angular-jwt','ngCookies']);
module.controller("WriterpadController", [ "$scope", "WriterpadService","jwtHelper",'$cookies',
		function($scope, WriterpadService,jwtHelper,$cookies) {


//$scope.expToken = 'eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ3cml0ZXJwYWQiLCJzdWIiOiJoaW1hbmsiLCJST0xFIjoiV1JJVEVSIiwiaWF0IjoxNTgyMDQ4MjYwLCJleHAiOjE1ODIwNDgzODB9.XPvW_7LSvkLZp5_mu2uPINtbdyuqssWiLtkoxccIYubmz566Tb2BC1J5tKvCUZOl8JqYyB97zfeybS1UnW2d4A';

//$scope.expToken=$cookies.get('AUTH-TOKEN');

//console.log($scope.expToken);
   // $scope.decodedJwt = jwtHelper.decodeToken($scope.expToken);



   // console.log("hello--",JSON.stringify($scope.decodedJwt));
   // console.log("yes--",$scope.decodedJwt.ROLE);

			$scope.userDto = {
				username : null,
				password : null,
				email : null,
				role : null
			};

				$scope.articleDto = {
            				title : null,
            				description : null,
            				body : null,
            				tags : null,
            				featuredImageUrl : null
            			};


	WriterpadService.getAllArticles().then(function(value) {
            						$scope.allArticles= value.data;
            							console.log(value.data);
                                    }, function(reason) {
                                          		console.log("error occured");
                                     }, function(value) {
                                     console.log("no callback");
                                    });





			$scope.saveUser = function() {

				WriterpadService.saveUser($scope.userDto).then(function() {
					console.log("works");

		$scope.userDto = {
    				username : null,
    				password : null,
    				email : null,
    				role : null
    			};
				}, function(reason) {
					console.log("error occured");
				}, function(value) {
					console.log("no callback");
				});
			}

						$scope.saveArticle = function() {

            				WriterpadService.saveArticle($scope.articleDto).then(function() {
            					console.log("works");
            					WriterpadService.getAllArticles().then(function(value) {
            						$scope.allArticles= value.data;
            					}, function(reason) {
            						console.log("error occured");
            					}, function(value) {
            						console.log("no callback");
            					});

      				$scope.articleDto = {
                  				title : null,
                  				description : null,
                  				body : null,
                  				tags : null,
                  				featuredImageUrl : null
                  			};
            				}, function(reason) {
            					console.log("error occured");
            				}, function(value) {
            					console.log("no callback");
            				});
            			}
		} ]);