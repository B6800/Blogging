
import React, { useContext,useState,useEffect } from "react";
import  AppContext  from "../AppContext.js";
import { likePost,unlikePost,deletePost ,addComment,getComments} from "../api.js";

function timeAgo(ts) {
    const diff = Date.now() - ts;
    const mins = Math.floor(diff / (60 * 1000));
    if (mins < 60) return `${mins}m`;
    const hours = Math.floor(mins / 60);
    if (hours < 24) return `${hours}h`;
    return `${Math.floor(hours / 24)}d`;
}
function renderWithHashtags(text) {
    return text.split(/(#[a-zA-Z0-9_]+)/g).map((part, idx) =>
        /^#[a-zA-Z0-9_]+$/.test(part) ? (
            <span key={idx} style={{
                color: "#b266ff",
                fontWeight: 600,
                cursor: "pointer"
            }}>{part}</span>
        ) : part
    );
}

export default function PostCard({ post }) {
    const {  user, posts, setPosts } = useContext(AppContext);
    const [notification, setNotification] = useState("");
    const [comments, setComments] = useState([]);
    const [commentText, setCommentText] = useState("");
    const [loadingComments, setLoadingComments] = useState(true);
    const likedByUser= post.likedByCurrentUser;
    const isOwner= post.ownedByCurrentUser;

    const avatarSrc = post.avatar || "https://api.dicebear.com/7.x/thumbs/svg?seed=placeholder";
    useEffect(() => {
        setLoadingComments(true);
        getComments(post.id)
            .then(setComments)
            .catch(() => setComments([]))
            .finally(() => setLoadingComments(false));
    }, [post.id]);

    async function handleAddComment(e) {
        e.preventDefault();
        if (commentText.trim().length === 0) return;
        try {
            await addComment(post.id, user.username, commentText);
            // After adding, reload comments from backend
            const updated = await getComments(post.id);
            setComments(updated);
            setCommentText("");
        } catch (e) {
            setNotification("Failed to add comment: " + e.message);
            setTimeout(() => setNotification(""), 2000);
        }
    }

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
                    src={avatarSrc}
                    alt={post.username || "user"}
                />
                <span className="post-username">{post.username}</span>
                <span className="post-time">{timeAgo(post.timestamp)}</span>
            </div>
            <div className="post-content">{renderWithHashtags(post.text)}</div>
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

                {isOwner && (
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
            {/* Comments section */}
            <div className="comments-section" style={{ marginTop: 12 }}>
                <form onSubmit={handleAddComment} style={{ display: "flex", gap: 6 }}>
                    <input
                        type="text"
                        placeholder="Write a comment…"
                        value={commentText}
                        onChange={e => setCommentText(e.target.value)}
                        style={{
                            flex: 1,
                            borderRadius: 8,
                            border: "1px solid #d2c4e8",
                            padding: 8
                        }}
                    />
                    <button type="submit" style={{ borderRadius: 8, padding: "8px 12px" }}>Comment</button>
                </form>
                <div className="comments-list" style={{ marginTop: 8 }}>
                    {loadingComments
                        ? <div style={{ color: "#aab8ff" }}>Loading comments…</div>
                        : comments.map((c, i) => (
                            <div key={i} style={{ fontSize: 14, color: "#b7cdfa", marginBottom: 2 }}>
                                <b>{c.username}</b>: {c.text}
                            </div>
                        ))
                    }
                </div>
            </div>
        </article>
    );
}
