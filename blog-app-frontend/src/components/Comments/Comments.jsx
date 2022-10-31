import React, { useCallback, useEffect } from 'react'
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import { Button, Col, Container, Modal, Row, Spinner } from 'reactstrap';
import { isLogin } from '../../Auth/AuthLogin';
import CommentsService from '../../services/CommentsService';

import { PrintTimeStamp } from '../../Util/DateTimeUtil';
import AddComments from './AddComments';

export default function Comments(props) {

    let { postId } = props;

    const [comments, setComments] = React.useState([]);
    const [loading, setLoading] = React.useState(true);
    const toggle = () => setLoading(!loading);
    const [error, setError] = React.useState({
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
    }, [error]);

    const LoadingComments = useCallback(() => {
        CommentsService.getAllCommentsByPostId(postId).then((data) => {
            setComments(data);
            setLoading(false);
        }).catch((err) => {
            setError({
                isError: true,
                error: err.response.message
            });
        })
        setLoading(false);
    }, [postId])

    useEffect(() => {
        setLoading(true);
        LoadingComments();

    }, [LoadingComments])




    return (
        <div>
            <Modal
                isOpen={loading}
                centered={true}
                backdrop="static"
                toggle={toggle}
                size="sm"
                keyboard={false}

            >

                <Button
                    color="primary"
                    disabled
                >
                    <Spinner size="sm">
                        Loading...
                    </Spinner>
                    <span>
                        {' '}Loading
                    </span>
                </Button>
            </Modal>
            <Container className='mt-3'>
                <Row>
                    <Col md={{
                        size: 9,
                        offset: 1
                    }}>
                        <h3>Reviews ({comments ? comments?.length : ""})</h3>
                        <hr />
                        {isLogin() ?
                            <AddComments postId={postId} LoadingComments={LoadingComments} />
                            :
                            <div className='text-muted container card-body' >Login to add a review <Button className='btn btn btn-outline-dark text-white mx-3'>Login</Button></div>
                        }
                        {comments?.length > 0 ?
                            comments?.map((comment, index) => {
                                return (
                                    <div key={index} className='comment-container card mt-2 shadow'>
                                        <div className='comment-header card-header flex'>
                                            <div className='comment-author'>{comment?.author}</div>
                                            <div className='comment-date'>{PrintTimeStamp(comment?.createdAt)}</div>
                                        </div>
                                        <div className='comment-body card-body'>
                                            {comment?.content}
                                        </div>
                                    </div>
                                )
                            })
                            :
                            <div className='text-muted container card-body'>No reviews yet</div>
                        }
                    </Col>
                </Row>
            </Container>
        </div>
    )
}
