import React from 'react'
import { Card, CardBody, CardFooter, CardHeader, CardSubtitle, CardText, CardTitle } from 'reactstrap'
import ViewPostDetails from './ViewPostDetails'

export default function PostCard(props) {
    const { id, title, content, category, createdAt } = props.post

    const trimContent = (content) => {
        if (content.length > 100) {
            return content.replace(/<[^>]*>?/gm, '').substring(0, 100) + "..."
        }
        return content.replace(/(<([^>]+)>)/ig, '');
    }

    const printTimeStamp = (timestamp) => {
        const date = new Date(timestamp);
        return date.toLocaleDateString();
    }

    return (
        <Card key={id}
            className="border-0 bg-dark bg-opacity-75 text-light"
            style={{
                textAlign: "left",
                margin: "1rem auto",
                boxShadow: "0.5rem 0.5rem 0.5rem 0.1rem rgb(0 0 0/ 0.2)  "
            }}
        >
            <CardHeader style={{
                display: "flex",
                justifyContent: "space-between",
                flexWrap: "wrap",
                alignItems: "stretch",
            }} >
                <CardTitle ><h4>{title}</h4></CardTitle>
                <CardSubtitle style={{
                    fontSize: "0.9rem",
                    textTransform: "capitalize",
                    color: "rgb(255 247 247 / 58%)"
                }}><abbr title={category.description} style={{ textDecoration: "none" }} >{category?.title} </abbr> </CardSubtitle>
            </CardHeader>
            <CardBody>
                <CardText dangerouslySetInnerHTML={{ __html: trimContent(content) + "..." }}></CardText>
            </CardBody>
            <CardFooter style={{
                display: "flex",
                justifyContent: "space-between",
                alignContent: "Center",
                alignItems: "center",
                flexWrap: "wrap",
                flexDirection: "row"
            }}>
                <ViewPostDetails post={props?.post} />
                <div>
                    <sub style={{
                        fontSize: "0.9rem",
                        textTransform: "capitalize",
                        color: "rgb(255 247 247 / 58%)"

                    }}>Posted On :{printTimeStamp(createdAt)}</sub>
                </div>
            </CardFooter>
        </Card >
    )
}

