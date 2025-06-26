import React, { useContext,useState,useEffect } from "react";
import  AppContext  from "../AppContext.js";
import { followUser, unfollowUser,getFollowers } from "../api.js";
//commit changes
export default function UserListItem({ user }) {
    const { user:currentUser, users, setUsers,setFollowersVersion} = useContext(AppContext);
    const isFollowing = user.followedByCurrentUser;
    const [followers, setFollowers] = useState([]);  // State to hold followers count/list
    const [loadingFollowers, setLoadingFollowers] = useState(false);// Load followers on mount or when user changes

    useEffect(() => {
        setLoadingFollowers(true);
        getFollowers(user.id)
            .then(res => setFollowers(res))
            .catch(() => setFollowers([]))
            .finally(() => setLoadingFollowers(false));
    }, [user.id]);
// Defensive checks!
    if (!user) {
        // Render nothing or a loading message until users/currentUserId is available
        return null;
    }//commit

    async function handleFollow() {
        try {
            await followUser(currentUser.username, user.id);
            // Option 1: re-fetch users (or followers) from backend
            // Option 2: update users state locally
            setUsers(users.map(u =>
                u.id === user.id
                    ? { ...u, followedByCurrentUser: true }
                    : u
            ));
           const updatedFollowers = await getFollowers(user.id);
           setFollowers(updatedFollowers);
            setFollowersVersion(v => v + 1);
        } catch (e) {
            alert("Follow failed: " + e.message);
        }
    }
    async function handleUnfollow() {
        try {
            await unfollowUser(currentUser.username, user.id);
            setUsers(users.map(u =>
                u.id === user.id
                    ? { ...u, followedByCurrentUser: false }
                    : u
            ));
            // Refresh followers
            const updatedFollowers = await getFollowers(user.id);
            setFollowers(updatedFollowers);
            if (user.id === currentUser.id) {
                setFollowersVersion(v => v + 1);
            }
        } catch (e) {
            alert("Unfollow failed: " + e.message);
        }
    }
//do a commit here
    return (
        <div>
            @{user.username}{" "}
            <span>
                Followers: {loadingFollowers ? "Loading..." : followers.length}
            </span>
            {isFollowing ? (
                <button onClick={handleUnfollow}>Unfollow</button>
            ) : (
                <button onClick={handleFollow}>Follow</button>
            )}
        </div>
    );
}


