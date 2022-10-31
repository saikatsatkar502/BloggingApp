import React, { useEffect, useMemo, useRef, useState } from 'react'
import { Card, CardBody, CardFooter, CardHeader, Col, Container, Form, FormGroup, Input, Label, Row } from 'reactstrap'
import "../../Pages/Style/Button.css"
import JoditEditor from 'jodit-react';
import CatagoryService from '../../services/CatagoryService'
import UploadImage from './UploadImage';
import PostService from '../../services/PostService';
import { getCurrentUser } from '../../Auth/AuthLogin';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';
export default function AddPostComponent() {

    const navigate = useNavigate();
    const editor = useRef(null);
    const [post, setPost] = useState({
        title: "",
        content: "",
        categoryTitle: "",
        image: "default.png"
    })

    const reset = () => {
        setPost({
            title: "",
            content: "",
            categoryTitle: "",
            image: "default.png"
        })
    }
    const user = getCurrentUser();

    const [error, setError] = useState({
        error: {},
        status: false
    })

    const [category, setCategory] = React.useState([])

    useEffect(() => {
        CatagoryService.getAllCatagory().then((res) => {
            setCategory(res.data)
        }).catch((err) => {
            console.log(err)
            setError({
                error: err.response.data,
                status: true
            })
        })

    }, [])


    const onChangeHandler = (e) => {
        setPost({ ...post, [e.target.name]: e.target.value });
    };

    useEffect(() => {
        if (error.status) {
            toast.error(error.error?.message, {
                position: toast.POSITION.TOP_CENTER,
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,

            })
        }
    }, [error])

    const uploadData = (data) => {

    }

    const config = useMemo(() => ({
        readonly: false,
        theme: "dark",
        toolbarButtonSize: "small",
        preventDefault: true,
        limitChars: 5000,
        toolbar: {
            buttons: [
                'bold', 'italic', 'underline', 'strikethrough', 'superscript', 'subscript', 'outdent', 'indent', 'ul', 'ol', 'font', 'fontsize', 'brush', 'paragraph', 'image', 'video', 'table', 'link', 'align', 'undo', 'redo', 'hr', 'eraser', 'copyformat', 'symbol', 'fullsize', 'print', 'selectall', 'cut', 'copy', 'paste', 'source', 'about'
            ]

        }

    }), [])



    const onSubmitHandler = (e) => {
        e.preventDefault();
        if (post.title === "" || post.content === "" || post.catagory === "0") {
            toast.warn("Please fill all the fields", {
                position: toast.POSITION.TOP_RIGHT,
                autoClose: 2000
            })
        } else if (post.content.length > 10000) {
            toast.warn("post can contain upto 10000 charecters. ", {
                position: toast.POSITION.TOP_RIGHT,
                autoClose: 2000
            })
        } else {
            PostService.addpost(post, user.email).then((res) => {
                toast.success("Post added successfully", {
                    position: toast.POSITION.TOP_RIGHT,
                    autoClose: 3000,
                    hideProgressBar: false,
                    closeOnClick: true,
                })
                reset();
                setTimeout(() => {
                    navigate("/news-feeds");
                }, 3000);

            }).catch((err) => {
                setError({
                    error: err.response.data,
                    status: true
                })
            })

            console.log(post);
        }
    }


    return (
        <div>

            <Container>
                <Row>
                    <Col className='col-md-8 offset-md-2 text-capitalize'>
                        <Card inverse className=" Login-Card bg-dark bg-opacity-75">

                            <CardHeader>
                                <h3>Add New Post</h3>
                            </CardHeader>
                            <CardBody className='' style={{
                                textAlign: "left",
                                color: "black"
                            }}>

                                <Form onSubmit={onSubmitHandler}>

                                    <FormGroup floating>
                                        <Input
                                            type='text'
                                            name='title'
                                            id='title'
                                            placeholder='Enter Title'
                                            value={post.title}
                                            onChange={onChangeHandler}
                                        />
                                        <Label>Title</Label>
                                    </FormGroup>
                                    <FormGroup >
                                        {/* <Input
                                            type='textarea'
                                            name='content'
                                            id='content'
                                            placeholder='Enter Body'
                                            style={{ height: 100 }}
                                        />*/}
                                        <Label className='text-light'>Post Content</Label>
                                        <JoditEditor
                                            name='content'
                                            ref={editor}
                                            config={config}
                                            value={post.content}
                                            onChange={() => setPost({ ...post, content: editor.current.value })}
                                        />
                                    </FormGroup>
                                    <FormGroup style={{
                                        display: "flex",
                                        justifyContent: "space-between",


                                    }} >
                                        <Input
                                            type='select'
                                            name="categoryTitle"
                                            id="catagory"
                                            placeholder="Catagory"
                                            className='mr-2'
                                            onChange={onChangeHandler}


                                        >   <option value="0">Select Catagory</option>
                                            {category.map((category) => (

                                                <option key={category.id} value={category.title}>
                                                    {category.title}
                                                </option>


                                            ))}
                                        </Input>
                                        {/* <UploadImage fullscreen="sm" size="sm" keyboard backdrop="static" centered uploadData={uploadData} /> */}
                                    </FormGroup>
                                    <FormGroup>
                                        <Input
                                            type='submit'
                                            value='Add Post'
                                            className='btn btn-outline-primary register'
                                        />
                                    </FormGroup>

                                </Form>
                            </CardBody>
                            <CardFooter>

                            </CardFooter>
                        </Card>
                    </Col>
                </Row>
            </Container>
        </div>
    )
}
