import React, { useContext } from "react";
import { AppContext } from "../App";

/**
 * Sidebar component displays user profile info and navigation links.
 * Allows the user to switch between timeline, profile, and new post views,
 * and provides a logout button.
 */
export default function Sidebar() {
    // Get needed values and functions from context
    const {
        currentUserId,   // ID of the logged-in user
        users,           // Array of all users
        setFeedType,     // Setter to change the feed view
        setSelectedUserId, // Setter to select which user's posts to view
        handleLogout     // Function to log the user out
    } = useContext(AppContext);

    // Get the current user's data
    const user = users.find(u => u.id === currentUserId);

    return (
        <nav className="sidebar">
            {/* User profile section with avatar, name, and username */}
            <div className="profile">
                <img className="avatar" src={user.avatar} alt={user.username} />
                <div>
                    <strong>{user.name}</strong>
                    <span className="username">@{user.username}</span>
                </div>
            </div>
            {/* Navigation links for Home, Profile, and New Post */}
            <ul>
                <li onClick={() => { setFeedType('timeline'); setSelectedUserId(null); }}>
                    <span>🏠</span> Home
                </li>
                <li onClick={() => { setFeedType('myposts'); setSelectedUserId(null); }}>
                    <span>👤</span> Profile
                </li>
                <li onClick={() => { setFeedType('newpost'); setSelectedUserId(null); }}>
                    <span>➕</span> New Post
                </li>
            </ul>
            {/* Logout button */}
            <button className="logout-btn" onClick={handleLogout}>
                Log out
            </button>
        </nav>
    );
}
