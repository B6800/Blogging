//Mock API structure inspired by typical RESTful API design patterns for social platforms,
// e.g. Twitter API and Instagram Graph API (see Twitter docs, Instagram docs)
//User images are sourced from randomuser.me.
//COMMIT THIS FILE
const BASE = "http://localhost:8080/api";
// Helper for Basic Auth header (username, password)

// User Registration (no auth needed)
export async function register(username, password) {
    const response = await fetch(`${BASE}/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    });
    if (!response.ok) {
        // Try to parse JSON error message from backend
        let errorMsg = "Registration failed";
        try {
            const err = await response.json();
            errorMsg = err.error || errorMsg;
        } catch {
            // If backend didn't return JSON, stick with generic error
        }
        throw new Error(errorMsg);
    }
    return await response.json();
}

// User Login (no auth needed)
export async function login(username, password) {
    const response = await fetch(`${BASE}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    });
    if (!response.ok) {
        // Try to parse JSON error message from backend
        let errorMsg = "Login failed";
        try {
            const err = await response.json();
            // The backend should return: { error: "Wrong password" } or similar
            errorMsg = err.error || errorMsg;
        } catch {
            // If parsing fails, fallback to generic
        }
        throw new Error(errorMsg);
    }
    return await response.json(); // User info
}

// Get Timeline (auth required)
export async function getTimeline(username, limit=20) {
    Number.isInteger(Number(limit)) ? Number(limit) : 20;
    const response = await fetch(`${BASE}/posts/timeline?username=${encodeURIComponent(username)}&limit=${limit}`);
    if (!response.ok) throw new Error("Could not fetch timeline");
    return await response.json();
}

// Create a Post (auth required, pass text in body)
export async function createPost(username,  text) {
    const response = await fetch(`${BASE}/posts`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({ username,text })
    });
    if (!response.ok) throw new Error("Failed to create post");
    return await response.json();
}

// Like a Post (auth required)
export async function likePost(username, postId) {
    const response = await fetch(`${BASE}/posts/${postId}/like`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({username})
    });
    if (!response.ok) throw new Error("Failed to like post");
}

// Unlike a Post (auth required)
export async function unlikePost(username, postId) {
    const response = await fetch(`${BASE}/posts/${postId}/unlike`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({username})
    });
    if (!response.ok) throw new Error("Failed to unlike post");
}
// Delete a Post (auth required)
export async function deletePost(username, postId) {
    const response = await fetch(`${BASE}/posts/${postId}?username=${encodeURIComponent(username)}`, {
        method: "DELETE"
    });
    if (!response.ok) throw new Error("Failed to delete post");
}

// Get User's Posts (auth required)
export async function getUserPosts(username, password, userId,limit=20) {
    const response = await fetch(`${BASE}/posts/user/${userId}/posts?username=${encodeURIComponent(username)}&limit=${limit}`);
    {
        if (!response.ok) throw new Error("Could not fetch user posts");
        return await response.json();
    }
}
// Search Users (auth required)
export async function searchUsers(username,  query) {
    const response = await fetch(`${BASE}/users/search?username=${encodeURIComponent(username)}&query=${encodeURIComponent(query)}`);
    if (!response.ok) throw new Error("Search failed");
    return await response.json();
}

// Follow a user (auth required)
export async function followUser(username, userId) {
    const response = await fetch(`${BASE}/users/${userId}/follow?username=${encodeURIComponent(username)}`,
        {
            method: "POST"
        });
    if (!response.ok) throw new Error("Failed to follow user");
}

// Unfollow a user (auth required)
export async function unfollowUser(username,  userId) {
    const response = await fetch(`${BASE}/users/${userId}/unfollow?username=${encodeURIComponent(username)}`,
        {
            method: "POST"
        });
    if (!response.ok) throw new Error("Failed to unfollow user");
}

// List followers (auth required)
export async function getFollowers( userId) {
    const response = await fetch(`${BASE}/users/${userId}/followers`);
    if (!response.ok) throw new Error("Failed to get followers");
    return await response.json();
}
