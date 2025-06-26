
import React, { useState, useContext } from "react";
import AppContext  from "../AppContext.js";
import { createPost } from "../api.js";
/*
 * NewPostBox component allows the current user to create a new post.
 * The post is added to the list of posts in context state.
 */
export default function NewPostBox() {

    const { user, posts, setPosts, setFeedType} = useContext(AppContext);//commit
    const [text, setText] = useState("");
    const[loading,setLoading]= useState(false)
    // Defensive checks!
    if (!user){
        // Render nothing or a loading message until user is available
        return null;
    }//commit


    async function handleSubmit(e) {//commit change
        e.preventDefault();
        setLoading(true);
        try {
            const newPost = await createPost(user.username, text);
            setPosts([newPost, ...posts]);
            setText("");
            setFeedType("myposts");
        } catch (err) {
            alert("Failed to create post: " + err.message);
        } finally {
            setLoading(false);
        }
    }

    return (
        <form className="new-post-box" onSubmit={handleSubmit}>
      <textarea
          placeholder="What's on your mind?"
          value={text}
          onChange={e => setText(e.target.value)}
          required
          maxLength={280}
      />
            <button type="submit" disabled={loading}>Post</button>
        </form>
    );
}
