import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {useParams, useNavigate, Link} from 'react-router-dom';

function AuthorDetail() {
    const [author, setAuthor] = useState(null);
    const [isNew, setIsNew] = useState(false);
    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        if (id === 'new') {
            setIsNew(true);
            setAuthor({ fullName: '' });
        } else {
            axios.get(`/api/authors/${id}`)
                .then(response => setAuthor(response.data))
                .catch(error => console.error('Error fetching author:', error));
        }
    }, [id]);

    function handleSubmit(event) {
        event.preventDefault();
        const method = isNew ? 'post' : 'put';
        axios[method](`/api/authors${isNew ? '' : `/${id}`}`, author)
            .then(() => navigate('/authors'))
            .catch(error => console.error('Error saving author:', error));
    }

    return (
        author && (
            <div className="form-container">
                <h1>{isNew ? 'Add New Author' : 'Edit Author'}</h1>
                <form onSubmit={handleSubmit}>
                    <input type="hidden" value={author.id || ''} />
                    <div className="form-group">
                        <label htmlFor="fullName">Full Name:</label>
                        <input
                            type="text"
                            id="fullName"
                            value={author.fullName || ''}
                            onChange={e => setAuthor({ ...author, fullName: e.target.value })}
                            className="form-control"
                            required
                        />
                    </div>
                    <button type="submit" className="submit-button">Save</button>
                </form>
                <div className="button-group">
                    <Link to="/authors" className="button">Back to Author List</Link>
                    <Link to="/" className="button">Home</Link>
                </div>
            </div>
        )
    );
}

export default AuthorDetail;