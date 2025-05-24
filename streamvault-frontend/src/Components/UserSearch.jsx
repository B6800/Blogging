import React, { useState, useContext } from "react";
import { AppContext } from "../App";
import UserListItem from "./UserListItem";

export default function UserSearch() {
    const { users, currentUserId } = useContext(AppContext);
    const [q, setQ] = useState("");
    const searchRes = q.trim()
        ? users.filter(
            u => u.username.toLowerCase().includes(q.toLowerCase()) && u.id !== currentUserId
        )
        : [];

    return (
        <div className="user-search">
            <input
                type="search"
                placeholder="Search users…"
                value={q}
                onChange={e => setQ(e.target.value)}
            />
            <div className="user-search-results">
                {searchRes.map(u => <UserListItem key={u.id} user={u} />)}
            </div>
        </div>
    );
}