import React, { useContext } from "react";
import { AppContext } from "../App";
export default function UserListItem({ user }) {
    const {
        currentUserId, users, setUsers, setFeedType, setSelectedUserId
    } = useContext(AppContext);

    const currentUser = users.find(u => u.id === currentUserId);
    const isFollowing = currentUser.following.includes(user.id);

    function handleFollow() {
        if (isFollowing) {
            setUsers(users.map(u =>
                u.id === currentUserId
                    ? { ...u, following: u.following.filter(fid => fid !== user.id) }
                    : u.id === user.id
                        ? { ...u, followers: u.followers.filter(fid => fid !== currentUserId) }
                        : u
            ));
        } else {
            setUsers(users.map(u =>
                u.id === currentUserId
                    ? { ...u, following: [...u.following, user.id] }
                    : u.id === user.id
                        ? { ...u, followers: [...u.followers, currentUserId] }
                        : u
            ));
        }
    }

    function handleView() {
        setFeedType('user');
        setSelectedUserId(user.id);
    }

    return (
        <div className="user-list-item">
            <span className="user" onClick={handleView}>@{user.username}</span>
            <button className={`follow-btn ${isFollowing ? "following" : ""}`} onClick={handleFollow}>
                {isFollowing ? "Following" : "Follow"}
            </button>
        </div>
    );
}