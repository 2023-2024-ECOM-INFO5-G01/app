
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import './Note.css';

import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus, faPencilAlt, faTrash } from '@fortawesome/free-solid-svg-icons';
import { getNotesByPatientAndUser } from 'app/entities/note/note.reducer';
import { NoteCreate } from './NoteCreate';
import Modal from 'react-modal';
import { NoteEdit } from './NoteUpdate';
import { set, update } from 'lodash';
import { deleteEntity as deleteNote } from 'app/entities/note/note.reducer';
import { Button } from 'reactstrap';
export const Notes = ({ idprops }: { idprops: string }) => {
  const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();
  const { id } = useParams<'id'>();
  const [filter, setFilter] = useState('all'); 
  const [selectedDate, setSelectedDate] = useState(null); 
  const [modal, setModal] = useState(false);
  const [modal2, setModal2] = useState(false);
  const updateSuccess = useAppSelector(state => state.note.updateSuccess);
  const [notes, setNotes] = useState([]);
useEffect(() => {
    if (account && account.login) {
        dispatch(getNotesByPatientAndUser({ id: idprops, login: account.login }))
            .then(response => {
                setNotes((response.payload as any).data);
                console.log(notes);
            })
            .catch(error => {
                console.error('Une erreur s\'est produite :', error);
            });
    }
}, [account.login, dispatch,modal,modal2,updateSuccess]);

  const [showDatePicker, setShowDatePicker] = useState(false);

  const handleCalendarIconClick = () => {
    setShowDatePicker(!showDatePicker);
  };

  const handleDatePickerClose = () => {
    setShowDatePicker(false);
  };
  const toggle = () => setModal(!modal);
    
    const toggle2 = () => setModal2(!modal2);

    const handleDeleteNote = (noteId) => {
      dispatch(deleteNote(noteId))
        .then(() => {
          dispatch(getNotesByPatientAndUser({ id: idprops, login: account.login }))
            .then(response => {
              setNotes((response.payload as any).data);
            })
            .catch(error => {
              console.error('Une erreur s\'est produite :', error);
            });
        });
    };

  return (
    <div>
      {notes.map(note => (
        <div key={note.id} className="note">
          <div className="note-content">
            <h3 className="note-title">Titre:{note.titre}</h3>
            <p>{note.note}</p>
          </div>
          <div className="note-buttons">
    <Button className="note-button" onClick={toggle2}>
      <FontAwesomeIcon icon={faPencilAlt} />
    </Button>
    <NoteEdit modal={modal2} toggle={toggle2} patientId={idprops} idnote={note.id} />
  <button className="note-button" onClick={() => handleDeleteNote(note.id)}>
            <FontAwesomeIcon icon={faTrash} />
          </button>
</div>
        </div>
      ))}
<Button className="add-note-button" onClick={toggle}>
  <FontAwesomeIcon icon={faPlus} />
</Button>
        <NoteCreate modal={modal} toggle={toggle} patientId={idprops} />
      
    </div>
  );
}

export default Notes;