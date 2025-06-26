import React, { useContext,useState,useEffect } from "react";
import  AppContext  from "../AppContext.js";
import { getFollowers } from "../api.js";

/**
 * Sidebar component displays user profile info and navigation links.
 * Allows the user to switch between timeline, profile, and new post views,
 * and provides a logout button.
 */
export default function Sidebar() {
    // Get needed values and functions from context
    const {
        user,   // ID of the logged-in user
        setFeedType,     // Setter to change the feed view
        setSelectedUserId, // Setter to select which user's posts to view
        handleLogout, // Function to log the user out
        followersVersion,
    } = useContext(AppContext);
    const[followers,setFollowers]=useState([]);
    const [loadingFollowers, setLoadingFollowers] = useState(false);
    // Fetch followers on mount or when user changes
    useEffect(() => {
        setFollowers([]);
        if (user?.id) {
            setLoadingFollowers(true);
            getFollowers(user.id)
                .then(setFollowers)
                .catch(() => setFollowers([]))
                .finally(() => setLoadingFollowers(false));
        }
    },[user,followersVersion]);
    if ( !user) {
        // Show a loading spinner or nothing while users or currentUserId is not set
        return null;
    }//Do commit here

    return (
        <nav className="sidebar">
            {/* User profile section with avatar, name, and username */}
            <div className="profile">
                <img className="avatar small" src={user.avatar} alt={user.username} />
                <div>
                    <strong>{user.name}</strong>
                    <span className="username">@{user.username}</span>
                    <br/>
                    <span style={{ fontSize: 13, color: "#888" }}>
                        {loadingFollowers
                            ? "Loading followers…"
                            : `${followers.length} follower${followers.length === 1 ? "" : "s"}`}
                    </span>
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
