import React, { useEffect, useState } from 'react'
import { toast } from 'react-toastify';
import { Col, Container, Pagination, PaginationItem, PaginationLink, Row } from 'reactstrap';
import PostService from '../../services/PostService';
import ViewPosts from './ViewPosts';


export default function NewsFeed() {

    const [postContent, setPostContent] = useState({
        posts: [],
        lastPage: false,
        currentPage: 0,
        pageSize: 0,
        totalElements: 0,
        totalPages: 1

    });


    const [error, setError] = useState({
        error: {},
        errorStatus: false
    })

    useEffect(() => {
        if (error.errorStatus) {
            toast.error(error.error?.message, {
                position: toast.POSITION.TOP_RIGHT,
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,

            })
        }
    }, [error])

    useEffect(() => {

        handlePaginationClick(0);


    }, [])

    const handlePaginationClick = (pageSize = 0, pageNumber = 5) => {
        PostService.getAllPosts(pageSize, pageNumber).then((res) => {
            setPostContent({
                posts: res.content,
                lastPage: res.lastPage,
                currentPage: res.page,
                pageSize: res.size,
                totalElements: res.totalElements,
                totalPages: res.totalPages
            });
            window.scrollTo(0, 0);
        }).catch((err) => {
            setError({
                error: err.response.data,
                errorStatus: true
            })
        })
    }



    return (
        <div className='container-fluid' >
            <Row>
                <Col md={{
                    size: 10,
                    offset: 1
                }}>
                    <h4 style={{
                        textAlign: "left",

                    }}
                        className="text-muted"
                    >
                        Blog Count ( {postContent.totalElements} )
                    </h4>
                    {
                        postContent?.totalElements !== 0 ? (
                            <>
                                <ViewPosts content={postContent.posts} />
                                <Container className='my-3'>
                                    <Pagination size="md">
                                        <PaginationItem disabled={postContent.currentPage === 0} onClick={() => handlePaginationClick(0)} style={{ zIndex: "0" }}>
                                            <PaginationLink
                                                first
                                            />
                                        </PaginationItem>
                                        <PaginationItem disabled={postContent.currentPage === 0} onClick={() => handlePaginationClick(postContent.currentPage - 1)} style={{ zIndex: "0" }}>
                                            <PaginationLink
                                                previous />
                                        </PaginationItem>
                                        {
                                            [...Array(postContent.totalPages)].map((page, i) => (
                                                <PaginationItem onClick={() => handlePaginationClick(i)} active={i === postContent.currentPage} key={i} style={{ zIndex: "0" }} >
                                                    <PaginationLink >
                                                        {i + 1}
                                                    </PaginationLink>
                                                </PaginationItem>
                                            ))
                                        }
                                        <PaginationItem disabled={postContent.currentPage === postContent.totalPages - 1} onClick={() => handlePaginationClick(postContent.currentPage + 1)} style={{ zIndex: "0" }}>
                                            <PaginationLink
                                                next />
                                        </PaginationItem>
                                        <PaginationItem disabled={postContent.lastPage} onClick={() => handlePaginationClick(postContent.totalPages - 1)} style={{ zIndex: "0" }}>
                                            <PaginationLink
                                                last
                                            />
                                        </PaginationItem>
                                    </Pagination>
                                </Container>
                            </>

                        )
                            : (<>Nothing to display.</>)
                    }


                </Col>
            </Row>
        </div >
    )
}
