# writerpad
writerpad application

# circleci
[![CircleCI](https://circleci.com/gh/himankbatra/writerpad.svg?style=svg&circle-token=4913c8125efe64d93c7191e77be592df248b6f58)](https://circleci.com/gh/himankbatra/writerpad)


# WriterPad

## Day 1

### Story 1:  Create Spring Boot application

Cover how Spring Boot works under the hood

### Story 2: DIY: Add Checkstyle and Spotbugs to the project

- [ ] Integrate Checkstyle
- [ ] Integrate Spotbugs
- [ ] Integrate CircleCI

### Story 3: REST API to create an article

`POST /api/articles`

Example request body:

```JSON
{
    "title": "How to learn Spring Booot",
    "description": "Ever wonder how?",
    "body": "You have to believe",
    "tags": ["java", "Spring Boot", "tutorial"]
}
```

Required fields: `title`, `description`, `body`

Optional fields: `tags` as an array of Strings, and `featuredImage`

Tags should be saved as lowercase.

Response

1. It will return response code 201 if article is created successfully

2. It will return 400 if validation failed

3. It will return 500 if anything else happened

4. It will return created article as shown below.

   ```json
   {
       "id": "a98fd991e69a",
       "slug": "how-to-learn-spring-boot",
       "title": "How to learn Spring Booot",
       "description": "Ever wonder how?",
       "body": "You have to believe",
       "tags": ["java", "spring-boot","tutorial"],
       "createdAt": "2019-11-24T03:22:56.637Z",
       "updatedAt": "2019-11-24T03:48:35.824Z",
       "favorited": false,
       "favoritesCount": 0,
   }
   ```

## Day 2

### Story 4: Update an article

```
PATCH /api/articles/:slug-uuid
```

The body of the patch is shown below:

```json
{
    "title": "How to learn Spring Boot by building an app",
}
```

All the fields are optional.

The `slug` also gets updated when article is changed.

Returns the updated article.

```json
{
    "id": "a98fd991e69a",
    "slug": "how-to-learn-spring-boot-by-building-an-app",
    "title": "How to learn Spring Booot",
    "description": "Ever wonder how?",
    "body": "You have to believe",
    "tags": ["java", "spring-boot","tutorial"],
    "createdAt": "2019-11-24T03:22:56.637Z",
    "updatedAt": "2019-11-25T03:48:35.824Z",
    "favorited": false,
    "favoritesCount": 0,
}
```

### Story 5: Get an article

`GET /api/articles/:slug-uuid`

This returns single article

```json
{
    "id": "a98fd991e69a",
    "slug": "how-to-learn-spring-boot-by-building-an-app",
    "title": "How to learn Spring Booot",
    "description": "Ever wonder how?",
    "body": "You have to believe",
    "tags": ["java", "spring-boot","tutorial"],
    "createdAt": "2019-11-24T03:22:56.637Z",
    "updatedAt": "2019-11-25T03:48:35.824Z",
    "favorited": false,
    "favoritesCount": 0,
}
```

### Story 6: List articles

`GET /api/articles`

This should support pagination. 

### Story 7: Delete an article

```
DELETE /api/articles/:slug-uuid
```

## Day 3

### Story 8: Add comments to an article

```
POST /api/articles/:slug-uuid/comments
```

Example request body

```json
{
    "body": "Awesome tutorial!"
 }
```

This will return 201 if successfully created.

`body` is mandatory. Should return 400 if `body` is not present.

Application should check spam words in the body.  You can find list of English bad words from here - [Link](https://docs.google.com/spreadsheets/d/1hIEi2YG3ydav1E06Bzf2mQbGZ12kh2fe4ISgLg_UBuM/edit#gid=0)

If request contains bad words then you should return HTTP status 400. 

You should also store IP address from which comment was made in the comment database table.

### Story 9: Get comments for an article

```
GET /api/articles/:slug-uuid/comments
```

This will return multiple comments

```json
[
    {
        "id": 1,
        "createdAt": "2019-11-18T03:22:56.637Z",
        "updatedAt": "2019-11-18T03:22:56.637Z",
        "body": "Awesome article!",
	      "idAddress": "10.101.0.1"
    },
    {
        "id": 2,
        "createdAt": "2019-11-18T03:22:56.637Z",
        "updatedAt": "2019-11-18T03:22:56.637Z",
        "body": "Bad article!",
         "idAddress": "10.101.0.3"
    }
]
```

### Story 10: Delete comment for an article

```
DELETE /api/articles/:slug-uuid/comments/:id
```

Returns 204 on successfully deletion.

Returns 404 if article or comment does not exist.

## Day 4

### Story 10 : An Article is having a STATUS

A newly created article is in `DRAFT` status. It can be modified to a `PUBLISHED` status.

`GET /api/articles/:slug-uuid`

This returns single article with STATUS

```json
{
    "id": "a98fd991e69a",
    "slug": "how-to-learn-spring-boot-by-building-an-app",
    "title": "How to learn Spring Booot",
    "description": "Ever wonder how?",
    "body": "You have to believe",
    "tags": ["java", "spring-boot","tutorial"],
    "createdAt": "2019-11-24T03:22:56.637Z",
    "updatedAt": "2019-11-25T03:48:35.824Z",
    "favorited": false,
    "favoritesCount": 0,
    "status" : "DRAFT"
}
```

### Story 11 : Get Articles By Status

`GET /api/articles?status=DRAFT`

This should support pagination. 

### Story 12 : Publish Article

Add `POST /api/articles/:slug-uuid/PUBLISH` to publish an article. This will changes the status to PUBLISH and send a mail to your xebia mail id. Mail sending can be configured using `JavaMailSender`. The article status should be changed even if the mail sending throws an error.

Response

1. It will return response code 204 if article is PUBLISHED successfully

2. It will return 400 if article is already PUBLISHED

3. It will return 500 if anything else happened

## Day 5

### Story 13: Find time to read for an article

You have to define a REST endpoint that will find time to read for an article given its id

```
GET /api/articles/:slug-uuid/timetoread
```

This should return following response

```json
{
"articleId": "slug-uuid",
"timeToRead": {
 "mins" : 3,
 "seconds" : 50
   } 
}
```

The logic to calculate time to read is `total number of words / speed of average human`

The speed of average human should be configurable.

## Day 6

### Story 14: Generate Tag based metrics 

You have to define a REST endpoint that will provide all tags, provided in all articles, with their occurance.

```
GET /api/articles/tags
```

This should return following response

```json
[{ "tag" : "java", "occurence" : "10"},
{ "tag" : "spring", "occurance" : "5"},
{ "tag" : "tutorial", "occurance" : "2"}
]
```

You need to look into the tags marked in an article. Tags metrics should not differentiate based on casing. 'Java','java', 'JAVA', 'JaVA' etc, should be considered one for determining count.

## Day 7

### Story 15:  Favourite an article

You have to design it yourself

Favourite count should change

### Story 16:  Unfavourite an article

You have to design it yourself

Favourite count should change

## Day 8

### Story 17: Only logged in users can create an article

You need to define mapping between user and article in the domain classes.

The article response need to be changed to below.

```json
{
    "id": "a98fd991e69a",
    "slug": "how-to-learn-spring-boot-by-building-an-app",
    "title": "How to learn Spring Booot",
    "description": "Ever wonder how?",
    "body": "You have to believe",
    "tags": ["java", "spring-boot","tutorial"],
    "createdAt": "2019-11-24T03:22:56.637Z",
    "updatedAt": "2019-11-25T03:48:35.824Z",
    "favorited": false,
    "favoritesCount": 0,
    "author": {
    "username":"shekhargulati",
    "fullName" : "Shekhar Gulati"
    }
}
```

## Day 9

## Story 18: User can only update articles that they created

In this story, you have to fix the update functionality to consider user

Create two articles one by user `u1` and another by user `u2`. If `u2` tries to update article created by `u1` then they should get an error response `403`.

### Story 19: User can only delete articles that they created

In this story, you have to fix the delete functionality to consider user.

Create two articles one by user `u1` and another by user `u2`. If `u2` tries to delete article created by `u1` then they should get an error response `403`.

## Day 10

### Story 20: A user should not be able to submit article with same body twice

In this story, we are implementing a simple copy paste detector.  If a same or different user tries to submit article with the body that already exist then you should return bad request to the user.

For example if user `u1 ` creates an article with body `hello, world` and then `u1` or `u2` tries to create another article article with body `hello, world`  then HTTP 400 should be returned.

You have to do this check in both create and update API

You can use https://github.com/rrice/java-string-similarity library to find the text similarity. If two strings are similar i.e. their similarity score is greater than 70% then you should return 400.

### Story 21: Find the random image using the Unsplash API and make it featured image of the article

We want to add functionality to our API where we want to randomly assign image to our article.

Go to this https://source.unsplash.com/ to learn how to find random image. Save that in the image field of the article.

```json
{
    "id": "a98fd991e69a",
    "slug": "how-to-learn-spring-boot-by-building-an-app",
    "title": "How to learn Spring Booot",
    "description": "Ever wonder how?",
    "body": "You have to believe",
    "tags": ["java", "spring-boot","tutorial"],
    "createdAt": "2019-11-24T03:22:56.637Z",
    "updatedAt": "2019-11-25T03:48:35.824Z",
    "favorited": false,
    "favoritesCount": 0,
	  "image": "https://images.unsplash.com/photo-1575931140251-cefba6c2b4e9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max",
    "author": {
    "username":"shekhargulati",
    "fullName" : "Shekhar Gulati"
    }
}
```

## Day 11

### Story 22: Get user profile

```
GET /api/profiles/:username
```

A user should be able to view profile details for any user. Authentication is optional.

The profile response 

```
HTTP 200
```

```json
{
    "username": "shekhargulati",
    "following": false,
    "followerCount": 0,
    "followingCount": 0,
    "articles": [
        {
            "id": "slanldcwndl111",
            "title": "Hello, World!"
        }
    ]
}
```

### Story 23: Follow a user

```
POST /api/profiles/:username/follow
```

Authentication required, returns a profile mentioned above

No additional parameters are required.

### Story 24: Unfollow a user

```
DELETE /api/profiles/:username/follow
```

Authentication required. It returns a profile mentioned above.

No additional parameters are are required.

### Story 25: User Roles 

Add user roles to the application. Each user is associated with either of the follwoing roles :

- WRITER
- EDITOR
- ADMIN

As a WRITER I should be able to perform the following only :

- Create Article
- Update Article
- Read Article
- Create Comments
- Read Comments

As a EDITOR I should be able to perform the following only :

- Publish Article 
- Read Article
- Create Comments
- Read Comments

As an ADMIN I should be able to perform the following :

- Create User
- Delete Article
- Perform as WRITER
- Perform as EDITOR

### Story 26: Perform JWT token based Authentication for API

In the existing  system we have configured `basic` authentication  for `/api/*`. We need to remove `basic` authentication  and use `JWT` instead. The process would consist  of two parts :

- generate token using eixisting `form` login
- pass the generated token as `Bearer` authentication header.

Use `JJWT` library to configure the token encoding and decoding.

References :

- https://jwt.io/
- https://dzone.com/articles/implementing-jwt-authentication-on-spring-boot-api
- https://dev.to/keysh/spring-security-with-jwt-3j76

