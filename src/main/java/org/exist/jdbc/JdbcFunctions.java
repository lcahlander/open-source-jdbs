package org.exist.jdbc;

import org.exist.xquery.*;
import org.exist.xquery.value.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.exist.jdbc.JdbcModule.functionSignature;
import static org.exist.xquery.FunctionDSL.param;
import static org.exist.xquery.FunctionDSL.returns;

/**
 * Some very simple XQuery example functions implemented
 * in Java.
 */
public class JdbcFunctions extends BasicFunction {

    private static final Logger LOGGER = Logger.getLogger(JdbcFunctions.class.getName());

    private static final String FS_CLASS_AVAILABLE_NAME = "class-available";
    public static final String JDBC_DRIVER = "jdbcDriver";
    public static final String JDBC_DRIVER_PARAMETER_DESCRIPTION = "The class of the JDBC driver.";


    public static final FunctionParameterSequenceType JDBC_DRIVER_PARAM_CONST = param(JDBC_DRIVER, Type.STRING, Cardinality.EXACTLY_ONE, JDBC_DRIVER_PARAMETER_DESCRIPTION);


    static final FunctionSignature FS_CLASS_AVAILABLE = functionSignature(
            FS_CLASS_AVAILABLE_NAME,
            "Test JDBC Connection",
            returns(Type.BOOLEAN),
            JDBC_DRIVER_PARAM_CONST
    );

    /**
     *
     * @param context
     * @param signature
     */
    public JdbcFunctions(final XQueryContext context, final FunctionSignature signature) {
        super(context, signature);
    }

    /**
     *
     * @param args
     * @param contextSequence
     * @return
     * @throws XPathException
     */
    @Override
    public Sequence eval(final Sequence[] args, final Sequence contextSequence) throws XPathException {
        StringValue jdbcDriver = null;

        try { jdbcDriver = (StringValue) args[0].itemAt(0); } catch (Exception e) { };

        LOGGER.log(Level.INFO, "Processing function: " + getName().getLocalPart());
        switch (getName().getLocalPart()) {

            case FS_CLASS_AVAILABLE_NAME:
                return classAvailable(jdbcDriver);

            default:
                throw new XPathException(this, "No function: " + getName() + "#" + getSignature().getArgumentCount());
        }
    }

    /**
     *
     * @param jdbcDriver
     * @return
     */
    private Sequence classAvailable(final StringValue jdbcDriver) {
        try {
            if (Class.forName(jdbcDriver.getStringValue()) != null) {
                return BooleanValue.TRUE;
            } else {
                return BooleanValue.FALSE;
            }
        } catch (ClassNotFoundException e) {
            return BooleanValue.FALSE;
        }
    }

}
