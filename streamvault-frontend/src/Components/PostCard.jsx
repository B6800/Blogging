import React, { useContext } from "react";
import  AppContext  from "../AppContext.js";
import { likePost } from "../api.js";

function timeAgo(ts) {
    const diff = Date.now() - ts;
    const mins = Math.floor(diff / (60 * 1000));
    if (mins < 60) return `${mins}m`;
    const hours = Math.floor(mins / 60);
    if (hours < 24) return `${hours}h`;
    return `${Math.floor(hours / 24)}d`;
}

export default function PostCard({ post }) {
    const {  user, posts, setPosts } = useContext(AppContext);
    const likedByUser= post.likedByCurrentUser;
    // Defensive checks!
    if (!user) {
        // Render nothing or a loading message until users/currentUserId is available
        return null;
    }//Commit


    async function handleLike() {
        // Can't like own post or like again
        if (post.username=== user.username || likedByUser) return;
        try {
            await likePost(user.username,post.id);
            setPosts(posts.map(p=>p.id===post.id?{...p,likeCount: p.likeCount +1,likedByCurrentUser:true}:p));
        }catch (e){
            alert("Failed to like post:"+ e.message);
        }
    }
    return (
        <article className="post-card">
            <div className="post-header">
                <img
                    className="avatar small"
                    src={post.avatar || "https://api.dicebear.com/7.x/thumbs/svg?seed=placeholder"}
                    alt={post.username || "user"}
                />
                <span className="post-username">{post.username}</span>
                <span className="post-time">{timeAgo(post.timestamp)}</span>
            </div>
            <div className="post-content">{post.text}</div>
            <div className="post-footer">
        <span className="likes">
          {
              post.likeCount
          }
          {
              post.likeCount === 1 ? "Like" : "Likes"
          }
        </span>
                {/* Like button logic */}
                {
                    post.username !== user.username && !likedByUser && (
                    <button className="like-btn" onClick={handleLike} title="Like this post">
                        ❤️
                    </button>
                )}
                {/* Optionally show a disabled heart if already liked */}
                {post.username !== user.username && likedByUser && (
                    <span style={{marginLeft: 8, color: "#ffb6ba"}}>❤️</span>
                )}
            </div>
        </article>
    );
}
