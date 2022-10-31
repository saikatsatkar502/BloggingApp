import React, { useState } from 'react'
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';

export default function UploadImage(args) {
    const [modal, setModal] = useState(false);

    const [image, setImage] = useState(null)

    const toggle = () => setModal(!modal);

    const uploadHandler = (e) => {
        e.preventDefault();

        if (image === null) {
            alert("Please select an image")
        } else {

            args.uploadImage(image);

            toggle();
        }
    }


    return (
        <div>
            <Button color="danger" onClick={toggle}>
                Upload Image
            </Button>
            <Modal isOpen={modal} toggle={toggle} {...args}>
                <ModalHeader toggle={toggle}>Upload Image</ModalHeader>
                <ModalBody>

                    <input type="file" name="image" onChange={(e) => setImage(e.target.files[0])} />
                    <Button color="primary" onClick={() => uploadHandler}>Upload</Button>



                </ModalBody>
                <ModalFooter>

                    <Button color="secondary" onClick={toggle}>
                        Cancel
                    </Button>
                </ModalFooter>
            </Modal>
        </div>
    )
}
