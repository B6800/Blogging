import React, { useState, useContext } from "react";
import  AppContext from "../AppContext.js";
import { searchUsers } from "../api.js";
import UserListItem from "./UserListItem";

export default function UserSearch() {
    const { user,users,setUsers,setFeedType,setSelectedUserId } = useContext(AppContext);
    const [query, setQ] = useState("");
    const[loading,setLoading]=useState(false);
    // Defensive checks!
    if (!user  ) {
        // Render nothing or a loading message until users/currentUserId is available
        return null;
    }//commit
    async function handleSearch(e) {
        e.preventDefault();
        setLoading(true);
        try {
            const res = await searchUsers(user.username, query);
            setUsers(res);
        } catch  {
            setUsers([]);
        } finally {
            setLoading(false);
        }
    }
    return (//commit changed this to form
        <form onSubmit={handleSearch}>
            <input
                type="search"
                value={query}
                onChange={e => setQ(e.target.value)}
                placeholder="Search users…"
            />
            <button type="submit" disabled={loading}>Search</button>
            <div className="user-search-results">
                {users.map(u => <UserListItem key={u.id} user={u}  setFeedType={setFeedType} setSelectedUserId={setSelectedUserId} />)}
            </div>
        </form>
    );
}