import React from 'react'
import PostCard from './PostCard';

export default function ViewPosts(props) {

    const renderPosts = props.content?.map((post) => {
        return (
            <PostCard post={post} key={post.id} />
        );
    })


    return (
        <div>
            {renderPosts}
        </div>

    )
}
