import React, {useContext, useEffect} from "react";
import  AppContext  from "../AppContext.js";
import PostCard from "./PostCard";
import { getUserPosts } from "../api.js";
/**
 * PostFeed component displays a feed of posts based on the selected view.
 * - Shows the user's own posts, a selected user's posts, or an aggregated timeline.
 */
export default function PostFeed() {
    // Pull relevant state from context
    const {

        user,           // Array of all user objects
        posts, // Array of all post objects
        setPosts,
        feedType,        // Determines which type of feed to show: "myposts", "user", or aggregated
        selectedUserId   // User ID of a profile currently being viewed (if any)
    } = useContext(AppContext);
    useEffect(() => {
        if (feedType === "user" && selectedUserId) {
            getUserPosts(user.username, user.password,selectedUserId)
                .then(posts => setPosts(posts))
                .catch(() => setPosts([]));
        }
    }, [feedType, selectedUserId,user,setPosts]);
    // Defensive checks!
    if (!user) {
        // Render nothing or a loading message until users/currentUserId is available
        return null;
    }//Commit this

let feedPosts;
if(feedType==="myposts"){
    feedPosts = posts.filter(p => p.username === user.username);
} else if (feedType === "user" && selectedUserId) {
    // In this case, posts are already fetched from backend for selectedUserId
    feedPosts = posts;
} else {
    // Aggregated timeline: (show posts by users you follow or yourself)
    // If you implement following, update logic here as needed!
    feedPosts = posts;
}
    // Sort posts in reverse chronological order (newest first), then limit to the latest 20 posts
    feedPosts = [...feedPosts]
        .sort((a, b) => b.timestamp - a.timestamp)
        .slice(0, 20);

    return (
        <div>
            {/* Show empty state if no posts, else map posts to PostCard components */}
            {feedPosts.length === 0
                ? <div className="empty">No posts yet.</div>
                : feedPosts.map(post =>
                    <PostCard key={post.id} post={post} />
                )}
        </div>
    );
}
