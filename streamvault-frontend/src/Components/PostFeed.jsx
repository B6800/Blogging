import React, { useContext } from "react";
import { AppContext } from "../App";
import PostCard from "./PostCard";

/**
 * PostFeed component displays a feed of posts based on the selected view.
 * - Shows the user's own posts, a selected user's posts, or an aggregated timeline.
 */
export default function PostFeed() {
    // Pull relevant state from context
    const {
        currentUserId,   // ID of the currently logged-in user
        users,           // Array of all user objects
        posts,           // Array of all post objects
        feedType,        // Determines which type of feed to show: "myposts", "user", or aggregated
        selectedUserId   // User ID of a profile currently being viewed (if any)
    } = useContext(AppContext);

    let feedPosts = [];
    // Determine which posts to show based on feedType
    if (feedType === "myposts") {
        // Show only posts by the current user
        feedPosts = posts.filter(p => p.userId === currentUserId);
    } else if (feedType === "user" && selectedUserId) {
        // Show only posts by the selected user (profile view)
        feedPosts = posts.filter(p => p.userId === selectedUserId);
    } else {
        // Aggregated timeline: posts by users the current user follows (and themselves)
        const currentUser = users.find(u => u.id === currentUserId);
        // Create a set of IDs: current user + everyone they follow
        const following = new Set([currentUserId, ...(currentUser?.following || [])]);
        // Show posts only from those users
        feedPosts = posts.filter(p => following.has(p.userId));
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
