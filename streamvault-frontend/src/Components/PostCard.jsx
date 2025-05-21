import React, { useContext } from "react";
import { AppContext } from "../App";

function timeAgo(ts) {
    const diff = Date.now() - ts;
    const mins = Math.floor(diff / (60 * 1000));
    if (mins < 60) return `${mins}m`;
    const hours = Math.floor(mins / 60);
    if (hours < 24) return `${hours}h`;
    return `${Math.floor(hours / 24)}d`;
}

export default function PostCard({ post }) {
    const { currentUserId, users, posts, setPosts } = useContext(AppContext);

    const user = users.find(u => u.id === post.userId);
    const likedByUser = post.likes.includes(currentUserId);

    function handleLike() {
        // Can't like own post or like again
        if (post.userId === currentUserId || likedByUser) return;
        setPosts(posts.map(p =>
            p.id === post.id
                ? { ...p, likes: [...p.likes, currentUserId] }
                : p
        ));
    }
    return (
        <article className="post-card">
            <div className="post-header">
                <img className="avatar small" src={user.avatar} alt={user.username} />
                <span className="post-username">{user.username}</span>
                <span className="post-time">{timeAgo(post.timestamp)}</span>
            </div>
            <div className="post-content">{post.text}</div>
            <div className="post-footer">
        <span className="likes">
          {
              post.likes.length
          }
          {
              post.likes.length === 1 ? "Like" : "Likes"
          }
        </span>
                {/* Like button logic */}
                {
                    post.userId !== currentUserId && !likedByUser && (
                    <button className="like-btn" onClick={handleLike} title="Like this post">
                        ❤️
                    </button>
                )}
                {/* Optionally show a disabled heart if already liked */}
                {post.userId !== currentUserId && likedByUser && (
                    <span style={{marginLeft: 8, color: "#ffb6ba"}}>❤️</span>
                )}
            </div>
        </article>
    );
}
