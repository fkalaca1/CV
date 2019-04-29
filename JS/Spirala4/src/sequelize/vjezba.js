module.exports = (sequelize, type) => {
    return sequelize.define('Vjezba', {
        id: {
          type: type.INTEGER,
          primaryKey: true,
          autoIncrement: true
        },
        naziv: type.STRING,
        spirala: {
            type: type.BOOLEAN,
        }
    }, {
        indexes: [
            {
                unique: true,
                fields: ['naziv']
            }
        ]
    })
}