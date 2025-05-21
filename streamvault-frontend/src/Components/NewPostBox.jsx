
import React, { useState, useContext } from "react";
import { AppContext } from "../App";
/*
 * NewPostBox component allows the current user to create a new post.
 * The post is added to the list of posts in context state.
 */
export default function NewPostBox() {
    const { currentUserId, posts, setPosts, feedType, setFeedType } = useContext(AppContext);
    const [text, setText] = useState("");

    function handleSubmit(e) {
        e.preventDefault();
        if (!text.trim()) return;
        setPosts([
            {
                id: Date.now(),            // Unique post ID (using timestamp)
                userId: currentUserId,     // Author of the post
                text: text.trim(),         // Content of the post
                timestamp: Date.now(),     // Creation time
                likes: [],                 // Array to track user IDs who liked the post
            },
            ...posts,
        ]);
        setText("");
        setFeedType("myposts"); // Show your post
    }

    // Show only in timeline/newpost view
    if (feedType === "user") return null;

    return (
        <form className="new-post-box" onSubmit={handleSubmit}>
      <textarea
          placeholder="What's on your mind?"
          value={text}
          onChange={e => setText(e.target.value)}
          required
          maxLength={280}
      />
            <button type="submit">Post</button>
        </form>
    );
}
