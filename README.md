# ics-wtp-streamvault
📈 Extra Features
This project implements four extra features, as allowed by the course requirements, to improve the base grade.

1. Un-liking Posts
   Users can remove their like from any post they have previously liked.

Backend:

Dedicated endpoint: POST /api/posts/{postId}/unlike

Only the user who previously liked the post can unlike it.

Frontend:

The UI allows users to toggle likes and unlikes for each post.

2. Commenting on Posts
   Users can add comments to posts and view all comments for a given post.

Backend:

Endpoints:

POST /api/posts/{postId}/comments — Add a comment

GET /api/posts/{postId}/comments — Retrieve comments

Frontend:

Each post displays its comments and provides a form to add new comments.

3. Deleting One’s Own Posts
   Users can delete posts they have created.

Backend:

Endpoint: DELETE /api/posts/{postId}?username=...

Only the owner of a post is permitted to delete it; unauthorized deletion is prevented.

Frontend:

A delete button is available next to each post created by the logged-in user.

4. Tagging Posts Using a Hashtag Mechanism (Frontend Only)
   Users can add hashtags to their posts by including #hashtag patterns in the post text.

Frontend:

Hashtags are automatically detected in post content and styled as faint (or as links).

Backend:

No special backend implementation; hashtags are handled entirely in the frontend.

