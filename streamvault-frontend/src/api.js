//Mock API structure inspired by typical RESTful API design patterns for social platforms,
// e.g. Twitter API and Instagram Graph API (see Twitter docs, Instagram docs)
//User images are sourced from randomuser.me.
export const users = [
    {
        id: 1,
        username: 'alice',
        name: 'Alice Smith',
        avatar: 'https://randomuser.me/api/portraits/women/68.jpg',
        followers: [2], // id's of followers
        following: [3, 4],
    },
    {
        id: 2,
        username: 'bob',
        name: 'Bob Lee',
        avatar: 'https://randomuser.me/api/portraits/men/11.jpg',
        followers: [1],
        following: [1],
    },
    {
        id: 3,
        username: 'user123',
        name: 'User OneTwoThree',
        avatar: 'https://randomuser.me/api/portraits/men/25.jpg',
        followers: [1],
        following: [],
    },
    {
        id: 4,
        username: 'janedoe',
        name: 'Jane Doe',
        avatar: 'https://randomuser.me/api/portraits/women/31.jpg',
        followers: [1],
        following: [],
    },
    {
        id: 5,
        username: 'sampleuser',
        name: 'Sample User',
        avatar: 'https://randomuser.me/api/portraits/men/42.jpg',
        followers: [],
        following: [],
    },
];

export const posts = [
    {
        id: 101,
        userId: 3,
        text: 'Just setting up my account. #myfirstpost',
        timestamp: Date.now() - 40 * 60 * 1000, // 40 mins ago
        likes: [2, 4],
    },
    {
        id: 102,
        userId: 4,
        text: 'Enjoying the sunny weather today!',
        timestamp: Date.now() - 1 * 60 * 60 * 1000, // 1 hour ago
        likes: [1, 3, 2, 5, 4, 1, 1, 2, 3, 5, 1, 1],
    },
    {
        id: 103,
        userId: 5,
        text: 'This is another example post.',
        timestamp: Date.now() - 2 * 60 * 60 * 1000, // 2 hours ago
        likes: [3],
    },
    // Add more posts for realism
];
