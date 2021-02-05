import React, {useEffect, useState} from 'react';
import Card from 'react-bootstrap/Card';
import CardDeck from 'react-bootstrap/CardDeck';
import scales from './scales/scales.exercise.json';
import tsp from './tsp/tsp.exercise.json';
import img from './scales/scales.gif';
import Button from 'react-bootstrap/Button';
import {Link} from 'react-router-dom';
import Container from 'react-bootstrap/Container';

const holderImage = 'data:image/svg+xml;charset=UTF-8,%3Csvg%20width%3D%22321%22%20height%3D%22160%22%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20viewBox%3D%220%200%20321%20160%22%20preserveAspectRatio%3D%22none%22%3E%3Cdefs%3E%3Cstyle%20type%3D%22text%2Fcss%22%3E%23holder_177243efc81%20text%20%7B%20fill%3A%23999%3Bfont-weight%3Anormal%3Bfont-family%3A-apple-system%2CBlinkMacSystemFont%2C%26quot%3BSegoe%20UI%26quot%3B%2CRoboto%2C%26quot%3BHelvetica%20Neue%26quot%3B%2CArial%2C%26quot%3BNoto%20Sans%26quot%3B%2Csans-serif%2C%26quot%3BApple%20Color%20Emoji%26quot%3B%2C%26quot%3BSegoe%20UI%20Emoji%26quot%3B%2C%26quot%3BSegoe%20UI%20Symbol%26quot%3B%2C%26quot%3BNoto%20Color%20Emoji%26quot%3B%2C%20monospace%3Bfont-size%3A16pt%20%7D%20%3C%2Fstyle%3E%3C%2Fdefs%3E%3Cg%20id%3D%22holder_177243efc81%22%3E%3Crect%20width%3D%22321%22%20height%3D%22160%22%20fill%3D%22%23373940%22%3E%3C%2Frect%3E%3Cg%3E%3Ctext%20x%3D%22120.1796875%22%20y%3D%2287.5%22%3E321x160%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fsvg%3E';

const ExerciseView = () => {

    const exerciseData = [
        {
            key: scales.id,
            id: scales.id,
            imgLocation: img,
            title: scales.name,
            text: scales.description.join(' ')
        }, {
            key: tsp.id,
            id: tsp.id,
            imgLocation: tsp.imgLocation,
            title: tsp.name,
            text: tsp.description.join(' ')
        }
    ];

    return (
        <CardDeck className={'d-flex justify-content-center'}>
            {exerciseData.map(exercise => <ExerciseCard {...exercise}/>)}
        </CardDeck>
    );
};

const ExerciseCard = ({imgLocation, title, text, id}) => {
    return (
        <HoverableCard>
            <Card.Img variant="top" src={imgLocation} style={{height: '300px'}}/>
            <Card.Body>
                <Card.Title as={'h5'}>{title}</Card.Title>
                <Card.Text>{text}</Card.Text>
            </Card.Body>
            <Card.Footer>
                <Container className={'d-flex justify-content-center'}>
                    <Button
                        variant={'primary'}
                        className={'btn-dark-blue'}
                        as={Link} to={`/exercises/${id}`}>
                        Try it!
                    </Button>
                </Container>
            </Card.Footer>
        </HoverableCard>
    );
};

const HoverableCard = ({children}) => {

    const [cardStyle, setCardStyle] = useState({});
    const [hovered, setHovered] = useState(false);

    useEffect(() => {
        const normalCardStyle = {
            minWidth: '30rem',
            maxWidth: '321px',
            transition: '0.3s',
            marginBottom: '1%',
        };

        const hoveredCardStyle = {
            minWidth: '30rem',
            maxWidth: '321px',
            boxShadow: '0 8px 16px 0 rgba(0, 0, 0, 0.2)',
            transition: '0.3s',
            marginBottom: '1%',
        };
        if (hovered === true) {
            setCardStyle(hoveredCardStyle);
        } else
            setCardStyle(normalCardStyle);
    }, [hovered]);

    return (
        <Card onMouseOver={() => setHovered(true)}
              onMouseOut={() => setHovered(false)}
              style={{...cardStyle}}
              className={''}>
            {children}
        </Card>
    );
};

export default ExerciseView;