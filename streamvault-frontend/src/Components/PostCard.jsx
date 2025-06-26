import React, { useContext,useState } from "react";
import  AppContext  from "../AppContext.js";
import { likePost,unlikePost,deletePost } from "../api.js";

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
    const [notification, setNotification] = useState("");
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
    async function handleUnlike() {
        if (!post.likedByCurrentUser) return;
        try {
            await unlikePost(user.username, post.id);
            setPosts(posts.map(p => p.id === post.id ? { ...p, likeCount: p.likeCount - 1, likedByCurrentUser: false } : p));
        } catch (e) {
            alert("Failed to unlike post:" + e.message);
        }
    }
    async function handleDelete() {

        try {
            await deletePost(user.username, post.id);
            setPosts(posts.filter(p => p.id !== post.id));
            setNotification("Post deleted!");
            setTimeout(() => setNotification(""), 2000); // hide after 2s
        } catch (e) {
            setNotification("Delete failed: " + e.message);
            setTimeout(() => setNotification(""), 2000);
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
                {post.username !== user.username && post.likedByCurrentUser && (
                    <button className="like-btn" onClick={handleUnlike} title="Unlike this post">
                        💔
                    </button>
                )}
                {/* Delete button for your own post */}
                {post.ownedByCurrentUser && (
                    <button
                        className="delete-btn"
                        onClick={handleDelete}
                    >
                        🗑️
                    </button>
                )}
                {notification && (
                    <div className="notification">{notification}</div>
                )}
                {post.username !== user.username && likedByUser && (
                    <span style={{marginLeft: 8, color: "#ffb6ba"}}>❤️</span>
                )}
            </div>
        </article>
    );
}
