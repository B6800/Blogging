
// noinspection JSUnusedGlobalSymbols

import React, { useState,useEffect } from "react";
import Sidebar from "./Components/Sidebar";
import PostFeed from "./Components/PostFeed";
import UserSearch from "./Components/UserSearch";
import NewPostBox from "./Components/NewPostBox";
import LoginRegister from "./Components/LoginRegister.jsx";
import { login, getTimeline,register} from "./api.js";
import AppContext from "./AppContext.js";
import "./App.css";

export default function App() {
// commit message changed Components from using CurrentUser to user
    // Auth state
    const [users, setUsers] = useState([]);
    const [user, setUser] = useState(null); // user object: { username, password, id, ... }
    const [posts, setPosts] = useState([]);
    const [feedType, setFeedType] = useState("timeline");
    const [selectedUserId, setSelectedUserId] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [followersVersion, setFollowersVersion] = useState(0);

    // Fetch timeline after login
    useEffect(() => {
        if (user) {
            setLoading(true);
            setError("");
            getTimeline(user.username,20)
                .then(data => setPosts(data))
                .catch(() => setError("Failed to fetch timeline"))
                .finally(() => setLoading(false));
        }
    }, [user]);

    // Registration handler (implement with your real backend later)
    async function handleRegister(newUsername, newPassword) {
        try {
            await register(newUsername, newPassword);
            await handleLogin(newUsername, newPassword); // login after register!
        } catch (e) {
            setError("Registration failed: " + e.message);
        }
    }

    // Login handler with backend
    async function handleLogin(username, password) {
        try {
            const userDto = await login(username, password); // userDto: { id, username, avatar, ... }
            setUser({ ...userDto, password }); // Store password for API calls
            setError("");
        } catch (e) {
            setError("Login failed: " + e.message);
        }
    }

    // Logout clears state
    function handleLogout() {
        setUser(null);
        setPosts([]);
        setFeedType("timeline");
        setSelectedUserId(null);
    }

    // If not logged in, show login/register
    if (!user) {
        return (
            <LoginRegister
                onLogin={handleLogin}
                onRegister={handleRegister}
                error={error}
            />
        );
    }

    // Context value for all child components
    const value = {
         user,
        users,
        posts,
        setPosts,
        feedType,
        setFeedType,
        selectedUserId,
        setSelectedUserId,
        handleLogout,
        setUser, // To update user details if needed
        setUsers,
        followersVersion,
        setFollowersVersion,
    };


    return (
        <AppContext.Provider value={value}>
            <div className="app-container">
                <div className="sidebar-column">
                    <Sidebar />
                </div>
                <div className="feed-column">
                    <div className="main-feed">
                        {loading && <div>Loading timeline...</div>}
                        {error && <div style={{ color: "red" }}>{error}</div>}
                        <NewPostBox />
                        <PostFeed />
                    </div>
                </div>
                <div className="right-column">
                    <UserSearch />
                </div>
            </div>
        </AppContext.Provider>
    )
}