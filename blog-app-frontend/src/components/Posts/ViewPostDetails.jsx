import React, { useState } from 'react'
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap'
import { BASE_URL } from '../../Api/Api'
import './style/ViewPostDetails.css'
import '../Comments/styles/Comments.css'
import Comments from '../Comments/Comments'


export default function ViewPostDetails(props) {
    const [modal, setModal] = useState(false);
    const post = props.post;
    const toggle = () => setModal(!modal);
    const printTimeStamp = (timestamp) => {
        const date = new Date(timestamp);
        return date.toLocaleString();
    }
    return (
        <div >
            <Modal className='post-details ' isOpen={modal} toggle={toggle} fullscreen scrollable={true} size='xl' backdrop="static" centered={true} >
                <ModalHeader toggle={toggle} className='post-details__header'>
                    <div><h3 style={{
                        textTransform: 'capitalize'
                    }}>{post.title}</h3></div>
                    <div className="text-muted" style={{ fontSize: "0.9rem" }} >{post.category?.title}</div>
                </ModalHeader>
                <ModalBody className='post-details__body '>
                    <div className='container  ' style={{
                        textAlign: "left",
                        margin: "1rem auto",
                        display: "flex",
                        justifyContent: "flex-start",
                        alignItems: "stretch",
                        flexWrap: "nowrap",
                        gap: "1rem",
                        flexDirection: "column",
                        alignContent: "flex-start",

                    }}>

                        <div className="image-container mt-3 post-details__content">
                            <img
                                className='img-fluid shadow '
                                src={BASE_URL + 'post/view-image/' + post.image + '/?postId=' + post.id}
                                alt="post"
                                height={"50%"}
                                width={"50%"} />
                        </div>

                        <div dangerouslySetInnerHTML={{ __html: post.content }} style={{
                            fontSize: "1.2rem",
                            textAlign: "justify",
                            minHeight: "30vh",
                            overflowX: "inherit"

                        }}>

                        </div>
                        <div >
                            <sub className='text-muted  ' style={{ fontSize: "0.9rem", textTransform: "capitalize" }} >author: {post?.author?.name} <br></br> posted on : {printTimeStamp(post?.createdAt)}</sub>
                        </div>
                        {
                            modal && <Comments postId={post.id} />
                        }

                    </div>
                </ModalBody>
                <ModalFooter className='post-details__Footer'>

                    <Button color="secondary" onClick={toggle}>
                        Close
                    </Button>
                </ModalFooter>
            </Modal>
            <Button className='btn btn-outlined-dark' onClick={toggle}>Read More</Button>
        </div>

    )
}
