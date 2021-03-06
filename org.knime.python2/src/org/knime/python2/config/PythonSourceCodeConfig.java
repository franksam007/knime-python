/*
 * ------------------------------------------------------------------------
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ------------------------------------------------------------------------
 */

package org.knime.python2.config;

import java.util.Locale;
import java.util.Objects;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.python2.DefaultPythonCommand;
import org.knime.python2.PythonCommand;
import org.knime.python2.PythonVersion;
import org.knime.python2.extensions.serializationlibrary.SentinelOption;
import org.knime.python2.extensions.serializationlibrary.SerializationOptions;
import org.knime.python2.generic.SourceCodeConfig;
import org.knime.python2.kernel.PythonKernelOptions;
import org.knime.python2.prefs.PythonPreferences;

/**
 * A basic config for nodes concerned with Python scripting.
 *
 * @author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
 * @author Christian Dietz, KNIME GmbH, Konstanz, Germany
 * @author Clemens von Schwerin, KNIME GmbH, Konstanz, Germany
 */
public class PythonSourceCodeConfig extends SourceCodeConfig {

    private static final String CFG_PYTHON_VERSION_OPTION = "pythonVersionOption";

    static final String CFG_PYTHON2COMMAND = "python2Command";

    static final String CFG_PYTHON3COMMAND = "python3Command";

    private static final String CFG_CHUNK_SIZE = "chunkSize";

    private static final String CFG_CONVERT_MISSING_TO_PYTHON = "convertMissingToPython";

    private static final String CFG_CONVERT_MISSING_FROM_PYTHON = "convertMissingFromPython";

    private static final String CFG_SENTINEL_OPTION = "sentinelOption";

    private static final String CFG_SENTINEL_VALUE = "sentinelValue";

    /**
     * {@link #m_python2Command} and {@link #m_python3Command} are special in that they currently aren't configurable
     * via a Python scripting node's dialog but only using flow variables. If no respective flow variables are set,
     * their value is retrieved from the Python preferences.
     */
    private static final String INDICATE_FALLBACK_TO_PREFERENCES_COMMAND_VALUE = "";

    private PythonVersion m_pythonVersion = PythonPreferences.getPythonVersionPreference();

    /**
     * {@code null} means to fall back to {@link PythonPreferences#getPython2CommandPreference()}.
     */
    private PythonCommand m_python2Command = null;

    /**
     * {@code null} means to fall back to {@link PythonPreferences#getPython2CommandPreference()}.
     */
    private PythonCommand m_python3Command = null;

    private int m_chunkSize = SerializationOptions.DEFAULT_CHUNK_SIZE;

    private boolean m_convertMissingToPython = SerializationOptions.DEFAULT_CONVERT_MISSING_TO_PYTHON;

    private boolean m_convertMissingFromPython = SerializationOptions.DEFAULT_CONVERT_MISSING_FROM_PYTHON;

    private SentinelOption m_sentinelOption = SerializationOptions.DEFAULT_SENTINEL_OPTION;

    private int m_sentinelValue = SerializationOptions.DEFAULT_SENTINEL_VALUE;

    /**
     * Indicates whether Python 3 shall be used by Python scripting nodes that are configured by this config instance.
     *
     * @return {@code true} if Python 3 shall be used, {@code false} if Python 2 shall be used.
     */
    public boolean getUsePython3() {
        return m_pythonVersion.equals(PythonVersion.PYTHON3);
    }

    /**
     * @return The configured Python version.
     */
    public PythonVersion getPythonVersion() {
        return m_pythonVersion;
    }

    /**
     * @param pythonVersion The configured Python version.
     */
    public void setPythonVersion(final PythonVersion pythonVersion) {
        m_pythonVersion = pythonVersion;
    }

    /**
     * @return The Python 2 command to use. May be {@code null} in which case no specific Python 2 command is configured
     *         and one has to resort to - e.g., - the {@link PythonPreferences#getPython2CommandPreference() global
     *         preferences}.
     */
    public PythonCommand getPython2Command() {
        return m_python2Command;
    }

    /**
     * @param python2Command The Python 2 command to use. May be {@code null} in which case no specific Python 2 command
     *            is configured and one has to resort to - e.g., - the
     *            {@link PythonPreferences#getPython2CommandPreference() global preferences}.
     */
    public void setPython2Command(final PythonCommand python2Command) {
        m_python2Command = python2Command;
    }

    /**
     * @return The Python 3 command to use. May be {@code null} in which case no specific Python 3 command is configured
     *         and one has to resort to - e.g., - the {@link PythonPreferences#getPython3CommandPreference() global
     *         preferences}.
     */
    public PythonCommand getPython3Command() {
        return m_python3Command;
    }

    /**
     * @param python3Command The Python 3 command to use. May be {@code null} in which case no specific Python 3 command
     *            is configured and one has to resort to - e.g., - the
     *            {@link PythonPreferences#getPython3CommandPreference() global preferences}.
     */
    public void setPython3Command(final PythonCommand python3Command) {
        m_python3Command = python3Command;
    }

    /**
     *
     * @return The configured number of rows to transfer to/from Python per chunk of an input/output table.
     */
    public int getChunkSize() {
        return m_chunkSize;
    }

    /**
     *
     * @param chunkSize The configured number of rows to transfer to/from Python per chunk of an input/output table.
     */
    public void setChunkSize(final int chunkSize) {
        m_chunkSize = chunkSize;
    }

    /**
     * @return {@true} if missing values shall be converted to sentinel values on the way to Python. {@code false} if
     *         they shall remain missing.
     */
    public boolean getConvertMissingToPython() {
        return m_convertMissingToPython;
    }

    /**
     * @param convertMissingToPython {@true} to configure that missing values shall be converted to sentinel values on
     *            the way to Python. {@code false} if they shall remain missing.
     */
    public void setConvertMissingToPython(final boolean convertMissingToPython) {
        m_convertMissingToPython = convertMissingToPython;
    }

    /**
     * @return {@true} if missing values shall be converted to sentinel values on the way back from Python.
     *         {@code false} if they shall remain missing.
     */
    public boolean getConvertMissingFromPython() {
        return m_convertMissingFromPython;
    }

    /**
     * @param convertMissingFromPython {@true} to configure that missing values shall be converted to sentinel values on
     *            the way back from Python. {@code false} if they shall remain missing.
     */
    public void setConvertMissingFromPython(final boolean convertMissingFromPython) {
        m_convertMissingFromPython = convertMissingFromPython;
    }

    /**
     * @return The configured sentinel options to use (if applicable; see {@link #getConvertMissingToPython()} and
     *         {@link #getConvertMissingFromPython()}).
     */
    public SentinelOption getSentinelOption() {
        return m_sentinelOption;
    }

    /**
     * @param sentinelOption The configured sentinel options to use (if applicable; see
     *            {@link #getConvertMissingToPython()} and {@link #getConvertMissingFromPython()}).
     */
    public void setSentinelOption(final SentinelOption sentinelOption) {
        m_sentinelOption = sentinelOption;
    }

    /**
     * @return The configured sentinel value to use (only used if {@link #getSentinelOption()} is
     *         {@link SentinelOption#CUSTOM}).
     */
    public int getSentinelValue() {
        return m_sentinelValue;
    }

    /**
     * @param sentinelValue The configured sentinel value to use (only used if {@link #getSentinelOption()} is
     *            {@link SentinelOption#CUSTOM}).
     */
    public void setSentinelValue(final int sentinelValue) {
        m_sentinelValue = sentinelValue;
    }

    @Override
    public void saveTo(final NodeSettingsWO settings) {
        super.saveTo(settings);
        settings.addString(CFG_PYTHON_VERSION_OPTION, getPythonVersion().getId());
        settings.addString(CFG_PYTHON2COMMAND, commandToString(getPython2Command()));
        settings.addString(CFG_PYTHON3COMMAND, commandToString(getPython3Command()));
        settings.addInt(CFG_CHUNK_SIZE, getChunkSize());
        settings.addBoolean(CFG_CONVERT_MISSING_TO_PYTHON, getConvertMissingToPython());
        settings.addBoolean(CFG_CONVERT_MISSING_FROM_PYTHON, getConvertMissingFromPython());
        settings.addString(CFG_SENTINEL_OPTION, getSentinelOption().name());
        settings.addInt(CFG_SENTINEL_VALUE, getSentinelValue());
    }

    @Override
    public void loadFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        super.loadFrom(settings);
        loadFromSettings(settings);
    }

    @Override
    public void loadFromInDialog(final NodeSettingsRO settings) {
        super.loadFromInDialog(settings);
        loadFromSettings(settings);
    }

    private void loadFromSettings(final NodeSettingsRO settings) {
        final String pythonVersionString = settings.getString(CFG_PYTHON_VERSION_OPTION, getPythonVersion().getId());
        // Backward compatibility: old saved versions may be all upper case.
        setPythonVersion(PythonVersion.fromId(pythonVersionString.toLowerCase(Locale.ROOT)));
        final String python2CommandString =
            settings.getString(CFG_PYTHON2COMMAND, commandToString(getPython2Command()));
        setPython2Command(commandFromString(python2CommandString));
        final String python3CommandString =
            settings.getString(CFG_PYTHON3COMMAND, commandToString(getPython3Command()));
        setPython3Command(commandFromString(python3CommandString));
        setChunkSize(settings.getInt(CFG_CHUNK_SIZE, getChunkSize()));
        setConvertMissingToPython(settings.getBoolean(CFG_CONVERT_MISSING_TO_PYTHON, getConvertMissingToPython()));
        setConvertMissingFromPython(
            settings.getBoolean(CFG_CONVERT_MISSING_FROM_PYTHON, getConvertMissingFromPython()));
        setSentinelOption(SentinelOption.valueOf(settings.getString(CFG_SENTINEL_OPTION, getSentinelOption().name())));
        setSentinelValue(settings.getInt(CFG_SENTINEL_VALUE, getSentinelValue()));
    }

    private static String commandToString(final PythonCommand command) {
        return command != null //
            ? command.toString() //
            : INDICATE_FALLBACK_TO_PREFERENCES_COMMAND_VALUE;
    }

    private static PythonCommand commandFromString(final String commandString) {
        return Objects.equals(commandString, INDICATE_FALLBACK_TO_PREFERENCES_COMMAND_VALUE) //
            ? null //
            // TODO: This only works for ordinary paths ("manual configuration"), not for Conda directory + environment
            // name ("Conda configuration").
            : new DefaultPythonCommand(commandString);
    }

    /**
     * Creates and returns a new {@link PythonKernelOptions} instance from the values stored in this config.
     *
     * @return The Python kernel options.
     */
    public PythonKernelOptions getKernelOptions() {
        final SerializationOptions serializationOptions = new SerializationOptions(getChunkSize(),
            getConvertMissingToPython(), getConvertMissingFromPython(), getSentinelOption(), getSentinelValue());
        return new PythonKernelOptions(getPythonVersion(), getPython2Command(), getPython3Command(),
            serializationOptions);
    }
}
