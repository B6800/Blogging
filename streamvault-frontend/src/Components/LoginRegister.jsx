
/*
 * LoginRegister component provides a UI for users to log in or register.
 *
 * Props:
 * - users: Array of user objects { id, username, password }
 * - onLogin: Function to call with user id when login is successful
 * - onRegister: Function to call with username and password when registration is successful
 */
import React, { useState } from "react";

export default function LoginRegister({ onLogin, onRegister, error }) {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [mode, setMode] = useState("login");

    async function handleLogin(e) {
        e.preventDefault();
        await onLogin(username.trim(), password); // parent handles error and state
    }

    async function handleRegister(e) {
        e.preventDefault();
        await onRegister(username.trim(), password);
        setMode("login"); // Switch to login mode after registration
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
