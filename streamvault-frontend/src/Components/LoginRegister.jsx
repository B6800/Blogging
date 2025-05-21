
import React, { useState } from "react";

/*
 * LoginRegister component provides a UI for users to log in or register.
 *
 * Props:
 * - users: Array of user objects { id, username, password }
 * - onLogin: Function to call with user id when login is successful
 * - onRegister: Function to call with username and password when registration is successful
 */
export default function LoginRegister({ users, onLogin, onRegister }) {
    // State for user input fields
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    // State for toggling between login and register modes
    const [mode, setMode] = useState("login");
    // State for error messages
    const [error, setError] = useState("");

    /**
     * Handles login form submission.
     * Validates if the user exists and password is correct.
     */
    function handleLogin(e) {
        e.preventDefault();
        const user = users.find(u => u.username === username.trim());
        if (!user) {
            setError("User not found. Please register.");
        } else if (user.password !== password) {
            setError("Incorrect password.");
        } else {
            setError("");
            onLogin(user.id);
        }
    }
    function handleRegister(e) {
        e.preventDefault();
        if (!username.trim() || !password) {
            setError("Enter username and password.");
        } else if (users.some(u => u.username === username.trim())) {
            setError("Username already exists.");
        } else {
            setError("");
            onRegister(username.trim(), password);
        }
    }

    return (
        <div className="login-page">
            <div className="login-card">
                <h2>{mode === "login" ? "Login" : "Register"}</h2>
                {/* Form handles login or register based on mode */}
                <form onSubmit={mode === "login" ? handleLogin : handleRegister}>
                    <input
                        placeholder="Username"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                        autoFocus
                    />
                    <input
                        placeholder="Password"
                        type="password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                    />
                    {/* Error message display */}
                    {error && <div className="error-msg">{error}</div>}
                    <button type="submit">
                        {mode === "login" ? "Login" : "Register"}
                    </button>
                </form>
                {/*Toggle between login and register*/}
                <div style={{ marginTop: 20 }}>
                    {mode === "login" ? (
                        <span>
                            New here?{" "}
                            <button
                                onClick={() => {
                                    setMode("register");
                                    setError("");
                                }}>
                                Register
                            </button>
                        </span>
                    ) : (
                        <span>
                            Already a user?{" "}
                            <button
                                onClick={() => {
                                    setMode("login");
                                    setError("");
                                }}>
                                Login
                            </button>
                        </span>
                    )}
                </div>
            </div>
        </div>
    );
}
