import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { getGenreById, createGenre, updateGenre } from '../services/genreService';

function GenreDetail() {
    const [genre, setGenre] = useState(null);
    const [isNew, setIsNew] = useState(false);
    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        if (id === 'new') {
            setIsNew(true);
            setGenre({ name: '' });
        } else {
            getGenreById(id)
                .then(response => setGenre(response.data))
                .catch(error => console.error('Error fetching genre:', error));
        }
    }, [id]);

    function handleSubmit(event) {
        event.preventDefault();
        const method = isNew ? createGenre : updateGenre;
        method(isNew ? genre : { ...genre, id })
            .then(() => navigate('/genres'))
            .catch(error => console.error('Error saving genre:', error));
    }

    return (
        genre && (
            <div className="form-container">
                <h1>{isNew ? 'Add New Genre' : 'Edit Genre'}</h1>
                <form onSubmit={handleSubmit}>
                    <input type="hidden" value={genre.id || ''} />
                    <div className="form-group">
                        <label htmlFor="name">Name:</label>
                        <input
                            type="text"
                            id="name"
                            value={genre.name || ''}
                            onChange={e => setGenre({ ...genre, name: e.target.value })}
                            className="form-control"
                            required
                        />
                    </div>
                    <button type="submit" className="submit-button">Save</button>
                </form>
                <div className="button-group">
                    <Link to="/genres" className="button">Back to Genre List</Link>
                    <Link to="/" className="button">Home</Link>
                </div>
            </div>
        )
    );
}

export default GenreDetail;