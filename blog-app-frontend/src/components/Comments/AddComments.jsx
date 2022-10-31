import React, { useState, useEffect } from 'react'

import { toast } from 'react-toastify';
import { Card, CardBody, CardFooter, CardHeader, CardSubtitle, CardTitle, Container, Input } from 'reactstrap'
import { getCurrentUser } from '../../Auth/AuthLogin';
import CommentsService from '../../services/CommentsService';

export default function AddComments({ postId, LoadingComments }) {
    const [comment, setComment] = useState({
        content: "",
    });

    const [error, setError] = useState({
        isError: false,
        error: ""
    });

    useEffect(() => {
        if (error.isError) {
            toast.error(error.error?.message, {
                position: "top-center",
                autoClose: 3000,
            });
        }

    }, [error])


    const userMail = getCurrentUser() === null ? "anonymous" : getCurrentUser().email;


    const handleSubmit = () => {

        CommentsService.addComment(comment, postId, userMail).then((res) => {
            toast.success("Comment Added", {
                position: "top-center",
                autoClose: 2000,
            });
            setTimeout(() => {
                setComment({
                    content: ""
                });
                LoadingComments();
            }, 3000);
        }
        ).catch((err) => {
            setError({
                isError: true,
                error: err.response.data.error
            });
        })
    }

    return (
        <Container className='mb-3' >
            <Card className='shadow'>
                <CardHeader style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center"


                }}>
                    <CardTitle>Post Your Comments : </CardTitle>
                    <CardSubtitle>{userMail}</CardSubtitle>
                </CardHeader>
                <CardBody>
                    <Input
                        type="textarea"
                        name="comment"
                        id="comment"
                        placeholder="Write your comment here"
                        value={comment.content}
                        onChange={(e) => setComment({ ...comment, content: e.target.value })}
                    />
                </CardBody>
                <CardFooter>
                    <Input type="button" className='btn btn-secondary' value="Submit" onClick={handleSubmit} />
                </CardFooter>

            </Card>

        </Container>
    )
}
