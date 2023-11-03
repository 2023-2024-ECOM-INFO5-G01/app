import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/epa">
        <Translate contentKey="global.menu.entities.epa" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ehpad">
        <Translate contentKey="global.menu.entities.ehpad" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/patient">
        <Translate contentKey="global.menu.entities.patient" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/albumine">
        <Translate contentKey="global.menu.entities.albumine" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/rappel">
        <Translate contentKey="global.menu.entities.rappel" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/poids">
        <Translate contentKey="global.menu.entities.poids" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/imc">
        <Translate contentKey="global.menu.entities.imc" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/repas">
        <Translate contentKey="global.menu.entities.repas" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/note">
        <Translate contentKey="global.menu.entities.note" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/aliment">
        <Translate contentKey="global.menu.entities.aliment" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
